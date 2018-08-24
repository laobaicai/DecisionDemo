package com.test.demo.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.text.TextUtils;
import android.util.Log;

import com.test.demo.bean.HotelBean;
import com.test.demo.constants.Constants;
import com.test.demo.constants.Status;
import com.test.demo.provider.DataDao;
import com.test.demo.services.MyIntentService;
import com.test.demo.util.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by caicai on 2017/8/29.
 */

public class DataHandler {
    private static final Map<Long, HotelBean> HOTEL_MAP = new ConcurrentHashMap<>();

    private static DataHandler mInstance = new DataHandler();

    private DataHandler() {

    }

    public static DataHandler getInstance() {
        return mInstance;
    }

    public void initData(Context context) {
        Log.e(Constants.DEBUG_TAG, "DataHandler: initData");
        if (HOTEL_MAP.isEmpty()) {
            Log.e(Constants.DEBUG_TAG, "DataHandler: init hotel data");
            initHotels(context);
        }
    }

    public void setAlarm(Context context) {
        if (!HOTEL_MAP.isEmpty()) {
            setHotelAlarm(context);
        }
    }

    public void setHotelAlarm(Context context) {
        Log.e(Constants.DEBUG_TAG, "setHotelAlarm");
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(Constants.HOTEL_ACTION);
        PendingIntent pi = PendingIntent.getService(context, Constants.HOTEL_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pi);
        // TODO 时间需要修改
    }

    public void cancelHotelAlarm(Context context) {
        Log.e(Constants.DEBUG_TAG, "cancelHotelAlarm");
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(Constants.HOTEL_ACTION);
        PendingIntent pi = PendingIntent.getService(context, Constants.HOTEL_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

    public void initHotels(Context context) {
        Map<Long, HotelBean> hotelMap = DataDao.getValidHotels(context);
        if (null != hotelMap) {
            HOTEL_MAP.clear();
            HOTEL_MAP.putAll(hotelMap);
            // log
            DataHandler.getInstance().mapLog();
        }
    }

    public void addHotel(Context context, long id) {
        HotelBean hotelBean = DataDao.getHotelBean(context, id);
        if (Status.NORMAL.getValue() == hotelBean.getStatus()) {
            HOTEL_MAP.put(id, hotelBean);
            setHotelAlarm(context);
        }
    }

    public void updateHotel(Context context, long id) {
        HotelBean newHotel = DataDao.getHotelBean(context, id);
        HotelBean oldHotel = null;
        if (HOTEL_MAP.containsKey(id)) {
            oldHotel = HOTEL_MAP.get(id);
            HOTEL_MAP.remove(id);
        }
        HOTEL_MAP.put(id, newHotel);

        // TODO 需要对比新老数据差异来判断是否需要重置alarm
        // 原来未缓存该数据的需重置alarm
        if (isHotelNeedReset(oldHotel, newHotel)) {
            setHotelAlarm(context);
        }
    }

    private boolean isHotelNeedReset(HotelBean oldHotel, HotelBean newHotel) {
        if (null == oldHotel) {
            Log.e(Constants.DEBUG_TAG, "oldHotel is null");
            return true;
        }
        if (!TextUtils.equals(oldHotel.getCheckInDate(), newHotel.getCheckInDate())) {
            Log.e(Constants.DEBUG_TAG, "hotel check in date is update, need reset");
            return true;
        }
        if (!TextUtils.equals(oldHotel.getCheckOutDate(), newHotel.getCheckOutDate())) {
            Log.e(Constants.DEBUG_TAG, "hotel check out date is update, need reset");
            return true;
        }
        if (!TextUtils.equals(oldHotel.getCheckInTime(), newHotel.getCheckInTime())) {
            Log.e(Constants.DEBUG_TAG, "hotel check in time is update, need reset");
            return true;
        }
        if (!TextUtils.equals(oldHotel.getCheckOutTime(), newHotel.getCheckOutTime())) {
            Log.e(Constants.DEBUG_TAG, "hotel check out time is update, need reset");
            return true;
        }
        return false;
    }

    public void deleteHotel(Context context, long id) {
        if (HOTEL_MAP.containsKey(id)) {
            HotelBean hotel = HOTEL_MAP.get(id);
            HOTEL_MAP.remove(id);
            if (HOTEL_MAP.isEmpty()) {
                cancelHotelAlarm(context);
            }
            // 删除日历
            ContentResolver cr = context.getContentResolver();
            long calendarId = hotel.getCalendarId();
            if (Utils.checkCalendarPermission(context) && calendarId > 0) {
                Uri eventDeleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, calendarId);
                int count = cr.delete(Reminders.CONTENT_URI, Reminders.EVENT_ID + "=?", new String[]{Long.toString(calendarId)});
                int row = cr.delete(eventDeleteUri, null, null);
                Log.e(Constants.DEBUG_TAG, "delete reminder:" + count + " delete event:" + row);
            }
        }
    }

    public void mapLog() {
        // log
        StringBuilder sb = new StringBuilder();
        for (HotelBean hotel : HOTEL_MAP.values()) {
            sb.append(hotel.toString()).append("\n");
        }
        Log.e(Constants.DEBUG_TAG, "mapLog:" + sb);
    }

    public Map<Long, HotelBean> getHotelCache() {
        return HOTEL_MAP;
    }
}
