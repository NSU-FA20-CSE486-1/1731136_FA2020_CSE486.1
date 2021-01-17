package com.ferdouszislam.nsu.cse486.sec01.alarmmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onShowNextAlarmClick(View view) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        try {

            long timeInMilliseconds = alarmManager.getNextAlarmClock().getTriggerTime();

            String userReadableTime = getUserReadableTimeFromMilliseconds(timeInMilliseconds);

            Toast.makeText(this, userReadableTime, Toast.LENGTH_SHORT)
                    .show();

        } catch (NullPointerException e){

            Toast.makeText(this, "No alarms set!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private String getUserReadableTimeFromMilliseconds(long timeInMilliseconds) {

        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
        Date result = new Date(timeInMilliseconds);

        return simple.format(result);
    }
}