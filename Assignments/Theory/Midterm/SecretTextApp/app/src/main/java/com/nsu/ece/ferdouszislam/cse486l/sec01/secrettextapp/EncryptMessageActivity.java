package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Activity to encrypt message and send via default SMS app
 */
public class EncryptMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_message);
    }
}