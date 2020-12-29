package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

public class CommonLoginActivity extends AppCompatActivity {

    private static final String TAG = "CLA-log";

    // selected user type
    private String mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login);
        
        init();
    }

    private void init() {

        setupToolbar();

        mUserType = getIntent().getStringExtra(UserTypeChoiceActivity.SELECTED_USER_TYPE_KEY);
        Log.d(TAG, "init: user type = "+mUserType);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.commonLogin_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // don't show default(app_name) title
        ab.setDisplayShowTitleEnabled(false);
    }
}