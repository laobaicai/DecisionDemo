package com.test.demo.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.test.demo.R;
import com.test.demo.bean.HotelBean;
import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;
import com.test.demo.constants.HotelTipType;
import com.test.demo.provider.DataDao;
import com.test.demo.task.DataHandler;
import com.test.demo.util.DateUtil;
import com.test.demo.util.Utils;

import java.util.Map;
import java.util.TimeZone;

/**
 * Created by caicai on 2017/9/4.
 */

public class MyIntentService extends IntentService {

    private static final long CALENDAR_ID = 1;

    private static final long HAS_ALARM = 1;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        Log.e(Constants.DEBUG_TAG, "MyIntentService action is: " + action);
        if (TextUtils.equals(Constants.HOTEL_ACTION, action)) {
            Map<Long, HotelBean> hotelList = DataHandler.getInstance().getHotelCache();
            // 遍历执行规则，如果没有calendarId则插入日历
            if (!hotelList.isEmpty()) {
                for (HotelBean hotel : hotelList.values()) {
                    addToCalendar(this, hotel);
                    // 判断规则，执行
                    handleNotification(hotel);
                }
            }
        }
    }

    private void addToCalendar(Context context, HotelBean hotel) {
        if (hotel.getCalendarId() > 0) {
            Log.e(Constants.DEBUG_TAG, "addToCalendar: hotel has calendarId");
            return;
        }
        if (!Utils.checkCalendarPermission(context)) {
            Log.e(Constants.DEBUG_TAG, "addToCalendar: has no permission");
            return;
        }

        // 添加event
        ContentValues eventValues = new ContentValues();
        eventValues.put(Events.TITLE, hotel.getHotelName());
        eventValues.put(Events.CALENDAR_ID, CALENDAR_ID); // 可能需要查询获取
        eventValues.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        if (!TextUtils.isEmpty(hotel.getHotelAddress())) {
            eventValues.put(Events.EVENT_LOCATION, hotel.getHotelAddress());
        }
        eventValues.put(Events.DTSTART, DateUtil.getMillis(hotel.getCheckInDate() + " " + hotel.getCheckInTime(), DateUtil.YYYY_MM_DD_HH_MM));
        eventValues.put(Events.DTEND, DateUtil.getMillis(hotel.getCheckOutDate() + " " + hotel.getCheckOutTime(), DateUtil.YYYY_MM_DD_HH_MM));
        eventValues.put(Events.HAS_ALARM, HAS_ALARM);

        Uri eventUri = context.getContentResolver().insert(Events.CONTENT_URI, eventValues);
        if (null == eventUri) {
            Log.e(Constants.DEBUG_TAG, "insert to events uri is null");
            return;
        }
        Log.e(Constants.DEBUG_TAG, eventUri.toString());

        // 将日历id更新到酒店数据库
        ContentValues values = new ContentValues();
        values.put(HotelColumns.CALENDAR_ID, ContentUris.parseId(eventUri));
        DataDao.updateHotelById(context, hotel.getId(), values);

        // 添加日程提醒
        ContentValues remindValues = new ContentValues();
        remindValues.put(Reminders.EVENT_ID, ContentUris.parseId(eventUri));
        remindValues.put(Reminders.MINUTES, Constants.TEN);
        remindValues.put(Reminders.METHOD, Reminders.METHOD_DEFAULT);

        Uri remindUri = context.getContentResolver().insert(Reminders.CONTENT_URI, remindValues);
        if (null == remindUri) {
            Log.e(Constants.DEBUG_TAG, "insert to reminder uri is null");
            return;
        }
        Log.e(Constants.DEBUG_TAG, remindUri.toString());
    }

    private void handleNotification(HotelBean hotel) {
        // 判断当前提醒类型
        // 最晚入住时间前半小时，弹入住提醒;最晚离店时间前半小时还在酒店1km内，弹离店提醒
        int type = hotel.getTipType();
        long current = System.currentTimeMillis();
        long checkIn = DateUtil.getMillis(hotel.getCheckInDate() + " " + hotel.getCheckInTime(), DateUtil.YYYY_MM_DD_HH_MM);
        long checkOut = DateUtil.getMillis(hotel.getCheckOutDate() + " " + hotel.getCheckOutTime(), DateUtil.YYYY_MM_DD_HH_MM);
        long diffIn = checkIn - current;
        long diffOut = Math.abs(checkOut - current);
        boolean isInHotel = Utils.isInOneKMArea(hotel.getLon(), hotel.getLat());
        Log.e(Constants.DEBUG_TAG, "handleNotification type:" + type + " diffIn:" + diffIn + " diffOut:" + diffOut);
        String title;
        String content;
        int newType;
        if (diffIn > 0 && diffIn <= DateUtil.THIRTY_MINUTES_MILLIS && type != HotelTipType.GO_OUT.getValue()) {
            title = getString(R.string.check_in_tip_title);
            content = getString(R.string.check_in_tip_content, hotel.getHotelName());
            newType = HotelTipType.GO_OUT.getValue();
        } else if (diffOut <= DateUtil.THIRTY_MINUTES_MILLIS && type != HotelTipType.CHECK_OUT.getValue() && isInHotel) {
            title = getString(R.string.check_out_tip_title);
            content = getString(R.string.check_out_tip_content, hotel.getHotelName());
            newType = HotelTipType.CHECK_OUT.getValue();
        } else {
            Log.e(Constants.DEBUG_TAG, "no need show notification");
            return;
        }

        // 更新数据库tips类型
        ContentValues values = new ContentValues();
        values.put(HotelColumns.TIP_TYPE, newType);
        DataDao.updateHotelById(this, hotel.getId(), values);

        // 发送通知
        Notification notification = new Notification.Builder(this).
                setContentTitle(title).
                setContentText(content).
                setSmallIcon(R.mipmap.ic_launcher_round).
                setAutoCancel(true).
                setTicker(title).
                setDefaults(Notification.DEFAULT_ALL).
                build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.HOTEL_NOTIFICATION_ID, notification);
    }

}
