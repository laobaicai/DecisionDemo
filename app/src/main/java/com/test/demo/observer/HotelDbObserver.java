package com.test.demo.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.test.demo.constants.Constants;
import com.test.demo.task.DataHandler;
import com.test.demo.util.Utils;

/**
 * Created by caicai on 2017/8/31.
 */

public class HotelDbObserver extends ContentObserver {
    private static final String INSERT = "insert";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private Context mContext;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public HotelDbObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.e(Constants.DEBUG_TAG, "HotelDbObserver onChange:" + uri.toString());
        String pathSegment = uri.getLastPathSegment();
        Log.e(Constants.DEBUG_TAG, "getLastPathSegment:" + pathSegment);
        if (!TextUtils.isEmpty(pathSegment) && pathSegment.contains("=")) {
            String[] strs = pathSegment.split("=");
            String option = strs[0];
            Long id = Utils.stringToLong(strs[1]);
            switch (option) {
                case INSERT:
                    DataHandler.getInstance().addHotel(mContext, id);
                    break;
                case UPDATE:
                    DataHandler.getInstance().updateHotel(mContext, id);
                    break;
                case DELETE:
                    DataHandler.getInstance().deleteHotel(mContext, id);
                    break;
                default:
                    break;
            }
            DataHandler.getInstance().mapLog();
        }
    }
}
