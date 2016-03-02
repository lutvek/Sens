package com.example.ludvig.sens;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdateSensorService extends IntentService {
    public UpdateSensorService() {
        super("UpdateSensorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("MyTestService", "Service running");
    }
}
