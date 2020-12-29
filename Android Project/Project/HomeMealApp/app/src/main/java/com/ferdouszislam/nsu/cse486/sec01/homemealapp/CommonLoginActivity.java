package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

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
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // don't show default(app_name) title
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    /*
    "Login" button click listener
     */
    public void loginClick(View view) {
    }

    public void dontHaveAnAccountClicked(View view) {

        if(mUserType.equals(UserType.CUSTOMER)){


        }

        else if(mUserType.equals(UserType.CHEF)){

            startActivity(new Intent(this, ChefSignupActivity.class));
        }

        else if(mUserType.equals(UserType.DELIVERY_GUY)){


        }

        else Log.d(TAG, "dontHaveAnAccountClicked: error! no such user type");
    }
}