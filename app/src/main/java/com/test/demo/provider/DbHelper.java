package com.test.demo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.test.demo.constants.BaseColumns;
import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;
import com.test.demo.constants.ServiceType;


/**
 * Created by gehengmin on 2017/8/22.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DB_NAME = "o2o.db";

    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateSql(ServiceType.HOTEL));
    }

    private String getCreateSql(ServiceType type) {
        String prefix = "CREATE TABLE IF NOT EXISTS ";
        StringBuilder sql = new StringBuilder().append(prefix);
        StringBuilder prefixColumn = new StringBuilder().append(BaseColumns._ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(BaseColumns.CREATE_TIME).append(" INTEGER,")
                .append(BaseColumns.MODIFY_TIME).append(" INTEGER,");
        switch (type) {
            case HOTEL:
                sql.append(HotelColumns.TABLE_NAME).append(" (")
                        .append(prefixColumn)
                        .append(HotelColumns.HOTEL_NAME).append(" TEXT NOT NULL,")
                        .append(HotelColumns.HOTEL_CITY).append(" TEXT,")
                        .append(HotelColumns.HOTEL_ADDRESS).append(" TEXT,")
                        .append(HotelColumns.CHECK_IN_DATE).append(" TEXT,")
                        .append(HotelColumns.CHECK_OUT_DATE).append(" TEXT,")
                        .append(HotelColumns.CHECK_IN_TIME).append(" TEXT,")
                        .append(HotelColumns.CHECK_OUT_TIME).append(" TEXT,")
                        .append(HotelColumns.ROOM_TYPE).append(" TEXT,")
                        .append(HotelColumns.ROOM_PRICE).append(" TEXT,")
                        .append(HotelColumns.CALENDAR_ID).append(" INTEGER,")
                        .append(HotelColumns.TIP_TYPE).append(" INTEGER")
                        .append(")");
                break;
            default:
                break;
        }
        Log.e(Constants.DEBUG_TAG, sql.toString());
        return sql.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
