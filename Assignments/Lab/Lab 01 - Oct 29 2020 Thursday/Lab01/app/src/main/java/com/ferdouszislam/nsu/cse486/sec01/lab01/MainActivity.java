package com.ferdouszislam.nsu.cse486.sec01.lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String ERROR_TAG = "mainActivity-error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showErrorLog();

    }

    private void showErrorLog() {
    // prints error log to logcat

        try{

            int x = 2/0;

        }catch (Exception e){

            Log.e(ERROR_TAG, "showErrorLog: error detected = "+e.getMessage(), e);

        }

    }
}