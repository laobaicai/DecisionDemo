package com.test.demo.task;

import android.content.Context;

import com.test.demo.bean.HotelBean;
import com.test.demo.util.DateUtil;

/**
 * Created by caicai on 2017/9/4.
 */

public class HotelRules {

    public static boolean isHotelValid(Context context, HotelBean hotelBean) {
        String currentDate = DateUtil.getDateString(context, System.currentTimeMillis(), DateUtil.YYYY_MM_DD);
        String checkOutDate = hotelBean.getCheckOutTime();

        return false;
    }
}
