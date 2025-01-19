package com.marusys.evo.audiotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CommandReceiver extends BroadcastReceiver {
    public final String TAG = "TAD-ZEILA";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Do some thing
        Log.d(TAG, "Received message:" + intent.getAction().toString());
        String msg = intent.getAction().toString();
        MainActivity.getInstance().onHandleBroadcast(msg);
    }
}