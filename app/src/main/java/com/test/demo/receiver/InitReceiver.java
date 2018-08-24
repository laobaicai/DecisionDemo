package com.test.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.test.demo.constants.Constants;
import com.test.demo.services.DecisionService;

/**
 * Created by caicai on 2017/8/21.
 */

public class InitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(Constants.DEBUG_TAG, "onReceive:" + Intent.ACTION_BOOT_COMPLETED);
            startDecisionService(context);
        }
    }

    private void startDecisionService(Context context) {
        Intent i = new Intent(context, DecisionService.class);
        context.startService(i);
    }
}
