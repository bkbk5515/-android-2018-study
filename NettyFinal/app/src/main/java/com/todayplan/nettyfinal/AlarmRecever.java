package com.todayplan.nettyfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmRecever extends BroadcastReceiver{

    private final String MYSERVICE_TAG = "bkbk5515";
    MainActivity activity;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MYSERVICE_TAG, "AlarmRecever()");
        activity = new MainActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(MYSERVICE_TAG, "AlarmRecever, 오레오이상");

            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
        } else {

            Log.d(MYSERVICE_TAG, "AlarmRecever, 오레오이하");

            Intent in = new Intent(context, MyService.class);
            context.startService(in);
        }
    }
}