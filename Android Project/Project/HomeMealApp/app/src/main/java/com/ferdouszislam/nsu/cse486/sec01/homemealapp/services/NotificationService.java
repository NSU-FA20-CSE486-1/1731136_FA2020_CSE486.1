package com.ferdouszislam.nsu.cse486.sec01.homemealapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service started!", Toast.LENGTH_SHORT)
                .show();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service ended!", Toast.LENGTH_SHORT)
                .show();
    }
}