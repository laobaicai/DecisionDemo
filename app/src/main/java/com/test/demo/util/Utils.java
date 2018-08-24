package com.test.demo.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/**
 * Created by gehengmin on 2017/8/31.
 */

public class Utils {
    public static Long stringToLong(String str) {
        Long lon = null;
        if (!TextUtils.isEmpty(str)) {
            lon = Long.valueOf(str);
        }
        return lon;
    }

    public static int stringToInt(String str) {
        int num = 0;
        if (!TextUtils.isEmpty(str)) {
            num = Integer.valueOf(str);
        }
        return num;
    }

    public static boolean checkCalendarPermission(Context context) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat
                .checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)) {
            return true;
        }
        // 权限弹窗：暂时不做，后续将需要的权限加到系统白名单中
        return false;
    }

    public static boolean isInOneKMArea(long lon, long lat) {
        return true;
    }
}
