package com.ferdouszislam.nsu.cse486.sec01.lab04;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    public void openSecondActivityClick(View view) {

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}