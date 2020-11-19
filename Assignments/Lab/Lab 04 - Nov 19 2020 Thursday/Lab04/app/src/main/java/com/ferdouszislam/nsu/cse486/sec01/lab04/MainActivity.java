package com.ferdouszislam.nsu.cse486.sec01.lab04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendButtonClick(View view) {

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void openThirdActivityClick(View view) {

        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}