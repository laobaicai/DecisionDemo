package com.test.demo.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.test.demo.constants.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gehengmin on 2017/9/4.
 */

public class DateUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String EIGHTEEN_OCLOCK = "18:00";

    public static final String TWELVE_OCLOCK = "12:00";

    public static final long THIRTY_MINUTES_MILLIS = 30 * 60 * 1000;



    public static String getDateString(Context context, long time, String format) {
        String dateStr = null;
        SimpleDateFormat sdf;
        if (!TextUtils.isEmpty(format)) {
            sdf = new SimpleDateFormat(format);
            dateStr = sdf.format(new Date(time));
        }
        return dateStr;
    }

    public static int dateStrCompare(Context context, String dateStr1, String dateStr2, String format1, String format2) {
        if (TextUtils.isEmpty(dateStr1) || TextUtils.isEmpty(dateStr2) || TextUtils.isEmpty(format1) || TextUtils.isEmpty(format2)) {
            return -1;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format1);
            Date date1 = sdf.parse(dateStr1);
            sdf.applyPattern(format2);
            Date date2 = sdf.parse(dateStr2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            Log.e(Constants.DEBUG_TAG, "dateStrCompare ParseException:" + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static long getMillis(String timeStr, String format) {
        if (TextUtils.isEmpty(timeStr) || TextUtils.isEmpty(format)) {
            return -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
