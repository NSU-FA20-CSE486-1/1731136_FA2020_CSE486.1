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

        long timeInMilliseconds = alarmManager.getNextAlarmClock().getTriggerTime();

        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
        Date result = new Date(timeInMilliseconds);
        String userReadableTime =  simple.format(result);

        Toast.makeText(this, userReadableTime, Toast.LENGTH_SHORT)
                .show();
    }
}