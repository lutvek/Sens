package com.example.ludvig.sens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.ludvig.sens.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UpdateSensorService.class);
        context.startService(i);
    }
}
