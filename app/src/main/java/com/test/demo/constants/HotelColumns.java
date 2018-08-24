package com.test.demo.constants;

import android.net.Uri;

import com.test.demo.provider.DataProvider;

/**
 * Created by caicai on 2017/8/22.
 */

public class HotelColumns implements BaseColumns {
    public static final String TABLE_NAME = "hotel";

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final Uri CONTENT_VALID_URI = Uri.withAppendedPath(CONTENT_URI, "valid");

    public static final String HOTEL_NAME = "hotel_name";

    public static final String HOTEL_CITY = "hotel_city";

    public static final String HOTEL_ADDRESS = "hotel_address";

    public static final String CHECK_IN_DATE = "check_in_date";

    public static final String CHECK_IN_TIME = "check_in_time";

    public static final String CHECK_OUT_DATE = "check_out_date";

    public static final String CHECK_OUT_TIME = "check_out_time";

    public static final String ROOM_TYPE = "room_type";

    public static final String ROOM_PRICE = "room_price";

    public static final String CALENDAR_ID = "calendar_id";

    public static final String TIP_TYPE = "tip_type";

}
