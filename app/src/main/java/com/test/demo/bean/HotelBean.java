package com.test.demo.bean;

import android.database.Cursor;

import com.google.gson.Gson;
import com.test.demo.MyApplication;
import com.test.demo.constants.HotelColumns;
import com.test.demo.constants.Status;
import com.test.demo.util.DateUtil;

/**
 * Created by caicai on 2017/8/29.
 */

public class HotelBean extends BaseBean {
    private String hotelName;
    private String hotelCity;
    private String hotelAddress;
    private String checkInDate;
    private String checkInTime;
    private String checkOutDate;
    private String checkOutTime;
    private String roomType;
    private String roomPrice;
    private long calendarId;
    private int tipType;
    private long lon;
    private long lat;
    private int status;

    public HotelBean(Cursor cursor) {
        super(cursor);
        hotelName = cursor.getString(cursor.getColumnIndex(HotelColumns.HOTEL_NAME));
        hotelCity = cursor.getString(cursor.getColumnIndex(HotelColumns.HOTEL_CITY));
        hotelAddress = cursor.getString(cursor.getColumnIndex(HotelColumns.HOTEL_ADDRESS));
        checkInDate = cursor.getString(cursor.getColumnIndex(HotelColumns.CHECK_IN_DATE));
        checkOutDate = cursor.getString(cursor.getColumnIndex(HotelColumns.CHECK_OUT_DATE));
        checkInTime = cursor.getString(cursor.getColumnIndex(HotelColumns.CHECK_IN_TIME));
        checkOutTime = cursor.getString(cursor.getColumnIndex(HotelColumns.CHECK_OUT_TIME));
        roomType = cursor.getString(cursor.getColumnIndex(HotelColumns.ROOM_TYPE));
        roomPrice = cursor.getString(cursor.getColumnIndex(HotelColumns.ROOM_PRICE));
        calendarId = cursor.getLong(cursor.getColumnIndex(HotelColumns.CALENDAR_ID));
        tipType = cursor.getInt(cursor.getColumnIndex(HotelColumns.TIP_TYPE));
        status = getStatus(this);
    }

    private int getStatus(HotelBean hotel) {
        int status = Status.NORMAL.getValue();
        String currentDate = DateUtil.getDateString(MyApplication.getInstance(), System.currentTimeMillis(), DateUtil.YYYY_MM_DD);
        int compare = DateUtil.dateStrCompare(MyApplication.getInstance(), currentDate, hotel.getCheckOutDate(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD);
        if (compare >= 0) {
            status = Status.NORMAL.getValue();
        }
        return status;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public int getTipType() {
        return tipType;
    }

    public void setTipType(int tipType) {
        this.tipType = tipType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
