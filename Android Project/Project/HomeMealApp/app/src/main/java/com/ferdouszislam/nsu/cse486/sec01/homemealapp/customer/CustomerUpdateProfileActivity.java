package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;

public class CustomerUpdateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use the same layout as "Customer Sign-up"
        setContentView(R.layout.activity_customer_signup);

        init();
    }

    private void init() {

        setupToolbar();

        findViewById(R.id.customerSignupWelcome_TextView).setVisibility(View.GONE);
        Button btn = findViewById(R.id.customerSignup_Button);
        btn.setText(getString(R.string.save));

        populateInputFields();
    }


    /**
     * populate the input fields with data of the
     * customer user's existing profile information
     * (the data should be downloaded from database)
     */
    private void populateInputFields() {
    }


    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerSignup_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.update_profile_title);
        }
    }
}