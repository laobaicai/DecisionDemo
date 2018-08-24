package com.test.demo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.test.demo.constants.Constants;
import com.test.demo.services.DecisionService;

/**
 * Created by caicai on 2017/8/21.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        mInstance = this;
        Log.e(Constants.DEBUG_TAG, "MyApplication onCreate");
        Intent i = new Intent(this, DecisionService.class);
        this.startService(i);
    }

}
