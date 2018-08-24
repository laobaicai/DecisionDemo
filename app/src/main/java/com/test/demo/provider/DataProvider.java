package com.test.demo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.demo.constants.BaseColumns;
import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;
import com.test.demo.util.DateUtil;

/**
 * Created by caicai on 2017/8/22.
 */

public class DataProvider extends ContentProvider {
    private static final String TAG = DataProvider.class.getSimpleName();
    public static final String AUTHORITY = "com.test.demo.provider.DataProvider";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final UriMatcher URI_MATCHER;
    private static final int HOTEL_CODE = 1;
    private static final int HOTEL_ID_CODE = 2;
    private static final int HOTEL_VALID_CODE = 3;
    private static final String HOTEL_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.provider.hotel";
    private static final String HOTEL_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.provider.hotel";
    private static final String HOTEL_CONTENT_VALID_TYPE = "vnd.android.cursor.dir/vnd.provider.hotel.valid";

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, HotelColumns.TABLE_NAME, HOTEL_CODE);
        URI_MATCHER.addURI(AUTHORITY, HotelColumns.TABLE_NAME + "/#", HOTEL_ID_CODE);
        URI_MATCHER.addURI(AUTHORITY, HotelColumns.TABLE_NAME + "/valid", HOTEL_VALID_CODE);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        mDb = mDbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case HOTEL_CODE:
                cursor = mDb.query(HotelColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HOTEL_ID_CODE:
                cursor = mDb.query(HotelColumns.TABLE_NAME, projection, HotelColumns._ID + "=?", selectionArgs, null, null, sortOrder);
                break;
            case HOTEL_VALID_CODE:
                String currentDate = DateUtil.getDateString(getContext(), System.currentTimeMillis(), DateUtil.YYYY_MM_DD);
                cursor = mDb.query(HotelColumns.TABLE_NAME, null, HotelColumns.CHECK_OUT_DATE + ">=?", new String[]{currentDate}, null, null, HotelColumns.CHECK_IN_DATE);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case HOTEL_CODE:
                return HOTEL_CONTENT_TYPE;
            case HOTEL_ID_CODE:
                return HOTEL_CONTENT_ITEM_TYPE;
            case HOTEL_VALID_CODE:
                return HOTEL_CONTENT_VALID_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        values.put(BaseColumns.CREATE_TIME, System.currentTimeMillis());
        values.put(BaseColumns.MODIFY_TIME, System.currentTimeMillis());
        if (!values.containsKey(HotelColumns.CHECK_IN_TIME)) {
            values.put(HotelColumns.CHECK_IN_TIME, DateUtil.EIGHTEEN_OCLOCK);
        }
        if (!values.containsKey(HotelColumns.CHECK_OUT_TIME)) {
            values.put(HotelColumns.CHECK_OUT_TIME, DateUtil.TWELVE_OCLOCK);
        }
        Log.e(Constants.DEBUG_TAG, "DataProvider insert:" + values.toString());
        switch (URI_MATCHER.match(uri)) {
            case HOTEL_CODE:
                long id = mDb.insert(HotelColumns.TABLE_NAME, null, values);
                if (id > 0) {
                    Uri insertUri = Uri.withAppendedPath(HotelColumns.CONTENT_URI, "insert=" + id);
                    getContext().getContentResolver().notifyChange(insertUri, null);
                    Log.e(Constants.DEBUG_TAG, "DataProvider insert:" + insertUri.toString());
                    return insertUri;
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = -1;
        switch (URI_MATCHER.match(uri)) {
            case HOTEL_CODE:
                long id = DataDao.getHotelId(getContext(), selection, selectionArgs);
                if (-1 != id) {
                    count = mDb.delete(HotelColumns.TABLE_NAME, selection, selectionArgs);
                    if (count > 0) {
                        Uri deleteUri = Uri.withAppendedPath(HotelColumns.CONTENT_URI, "delete=" + id);
                        getContext().getContentResolver().notifyChange(deleteUri, null);
                        Log.e(Constants.DEBUG_TAG, "DataProvider delete:" + deleteUri.toString());
                    }
                }
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        values.put(BaseColumns.MODIFY_TIME, System.currentTimeMillis());
        Log.e(Constants.DEBUG_TAG, "DataProvider update:" + values.toString());
        switch (URI_MATCHER.match(uri)) {
            case HOTEL_CODE:
                long id = DataDao.getHotelId(getContext(), selection, selectionArgs);
                if (-1 != id) {
                    count = mDb.update(HotelColumns.TABLE_NAME, values, selection, selectionArgs);
                    if (count > 0) {
                        Uri updateUri = Uri.withAppendedPath(HotelColumns.CONTENT_URI, "update=" + id);
                        getContext().getContentResolver().notifyChange(updateUri, null);
                        Log.e(Constants.DEBUG_TAG, "DataProvider update:" + updateUri.toString());
                    }
                }
                break;
            default:
                break;
        }
        return count;
    }
}
