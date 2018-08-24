package com.test.demo.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.test.demo.bean.HotelBean;
import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caicai on 2017/8/29.
 */

public class DataDao {

    public static Map<Long, HotelBean> getValidHotels(Context context) {
        ContentResolver cr = context.getContentResolver();
        Map<Long, HotelBean> map = null;
        Cursor cursor;
        try {
            cursor = cr.query(HotelColumns.CONTENT_VALID_URI, null, null, null, HotelColumns.CHECK_IN_DATE);
            if (null != cursor) {
                HotelBean hotel;
                map = new HashMap<>();
                while (cursor.moveToNext()) {
                    hotel = new HotelBean(cursor);
                    map.put(hotel.getId(), hotel);
                }
            }
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }
        return map;
    }


    public static long getHotelId(Context context, String selection, String[] selectionArgs) {
        long id = -1;
        Cursor cursor = context.getContentResolver().query(HotelColumns.CONTENT_URI, new String[]{HotelColumns._ID}, selection, selectionArgs, null);
        if (null != cursor && cursor.moveToNext()) {
            id = cursor.getLong(0);
        }
        return id;
    }


    public static HotelBean getHotelBean(Context context, long id) {
        HotelBean hotel = null;
        Cursor cursor = context.getContentResolver().query(HotelColumns.CONTENT_URI, null, HotelColumns._ID + "=?", new String[]{Long.toString(id)}, null);
        if (null != cursor && cursor.moveToNext()) {
            hotel = new HotelBean(cursor);
        }
        return hotel;
    }

    public static void updateHotelById(Context context, long id, ContentValues values) {
        if (id <=0 || null == values || 0 == values.size()) {
            Log.e(Constants.DEBUG_TAG, "updateHotelById: id is invalid or values id invalid");
            return;
        }
        Log.e(Constants.DEBUG_TAG, "updateHotelById id:" + id + " values:" + values.toString());
        ContentResolver cr = context.getContentResolver();
        cr.update(HotelColumns.CONTENT_URI, values, HotelColumns._ID + " = ?", new String[]{Long.toString(id)});
    }
}
