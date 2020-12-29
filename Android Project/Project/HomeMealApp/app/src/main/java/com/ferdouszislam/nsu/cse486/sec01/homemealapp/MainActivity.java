package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for welcome page
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    "GET STARTED" button click listener
     */
    public void getStartedClick(View view) {

        startActivity(new Intent(this, UserTypeChoiceActivity.class));

        // stop opening this activity on back press
        finish();
    }
}