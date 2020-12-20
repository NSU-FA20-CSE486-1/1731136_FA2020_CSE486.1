package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * Activity to decrypt an encrypted message
 */
public class DecryptMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_message);
    }

    public void decryptMessageClick(View view) {
    }
}