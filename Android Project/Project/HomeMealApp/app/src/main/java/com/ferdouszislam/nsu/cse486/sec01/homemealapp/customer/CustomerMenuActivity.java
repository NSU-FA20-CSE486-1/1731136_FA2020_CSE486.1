package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings.SettingsActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class CustomerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        init();
    }

    private void init() {

        setupToolbar();
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerMenu_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.menu_title);
        }
    }


    /*
    Menu option click listeners
     */

    public void placedOrdersClick(View view) {

        startActivity(new Intent(this, CustomerPlacedOrdersActivity.class));
    }

    public void updateProfileClick(View view) {

        startActivity(new Intent(this, CustomerUpdateProfileActivity.class));
    }

    public void logoutClick(View view) {

        SessionUtil.logoutNow(this, new FirebaseEmailPasswordAuthentication());
    }

    public void settingsClick(View view) {

        startActivity(new Intent(this, SettingsActivity.class));
    }
}