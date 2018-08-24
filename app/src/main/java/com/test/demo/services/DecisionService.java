package com.test.demo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;
import com.test.demo.observer.HotelDbObserver;
import com.test.demo.task.DataHandler;

/**
 * Created by caicai on 2017/8/28.
 */

public class DecisionService extends Service {
    private HotelDbObserver mHotelDbObserver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(Constants.DEBUG_TAG, "DecisionService onStartCommand");
        // 读取数据
        DataHandler.getInstance().initData(DecisionService.this);
        // 启动规则匹配
        Intent i = new Intent(this, MyIntentService.class);
        i.setAction(Constants.HOTEL_ACTION);
        this.startService(i);

        // 设置定时器
        DataHandler.getInstance().setAlarm(DecisionService.this);
        // 注册数据库监听
        if (null == mHotelDbObserver) {
            mHotelDbObserver = new HotelDbObserver(this, new Handler());
            this.getContentResolver().registerContentObserver(HotelColumns.CONTENT_URI, true, mHotelDbObserver);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(Constants.DEBUG_TAG, "DecisionService onDestroy");
        // 解除数据库监听
        if (null != mHotelDbObserver) {
            this.getContentResolver().unregisterContentObserver(mHotelDbObserver);
        }
    }
}
