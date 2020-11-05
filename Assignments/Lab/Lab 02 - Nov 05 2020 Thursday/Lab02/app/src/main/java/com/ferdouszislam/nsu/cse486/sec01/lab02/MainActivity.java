package com.ferdouszislam.nsu.cse486.sec01.lab02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// Controller class

    // model
    private Counter mCounter;

    // ui elements
    private TextView countTextView;

    // log tag
    private final String TAG = "MainActivity-Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();

    }

    private void init() {
        // initialize UI components and model

        // init model
        mCounter = new Counter(0);

        // init text_view
        countTextView = findViewById(R.id.text_view_count);
        countTextView.setText(Integer.toString(mCounter.getCount()));

        Log.d(TAG, "init: all variables initialized");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    // save data on screen rotation

        super.onSaveInstanceState(outState);

        // save the current count shown to user
        outState.putInt(Counter.COUNT_INSTANCE_ID, mCounter.getCount());

        Log.d(TAG, "onSaveInstanceState: count = "+mCounter.getCount()+" saved on screen rotation");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    // restore previously saved count before screen rotation

        super.onRestoreInstanceState(savedInstanceState);

        int previouslySavedCount = savedInstanceState.getInt(Counter.COUNT_INSTANCE_ID);
        mCounter = new Counter(previouslySavedCount);

        updateCountUI();

        Log.d(TAG, "onRestoreInstanceState: counter = "+mCounter.getCount()+" restored!");
    }


    public void showToastClick(View view) {
    // button_toast click handler
    // show a toast message

        Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG)
                .show();

    }

    public void countClick(View view) {
    // button_toast click handler
    // show a toast message

        // manipulate model
        mCounter.incrementCount();

        updateCountUI();

    }

    private void updateCountUI(){
    // show updated count in UI

        if(countTextView!=null) {
            // show changes in ui

            countTextView.setText(Integer.toString(mCounter.getCount()));
        }

        else{
            // unable to update ui

            mCounter.decrementCount();

            Log.e(TAG, "countClick: cannot update UI, counter text view is null!!");
        }

    }
}