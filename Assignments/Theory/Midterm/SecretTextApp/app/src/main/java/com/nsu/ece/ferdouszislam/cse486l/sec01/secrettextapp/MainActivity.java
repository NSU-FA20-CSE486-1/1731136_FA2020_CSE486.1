package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

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

    public void encryptMessageClick(View view) {

        startActivity(new Intent(this, EncryptMessageActivity.class));
    }

    public void decryptMessageClick(View view) {

        startActivity(new Intent(this, DecryptMessageActivity.class));
    }
}