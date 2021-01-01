package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefMenuActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefPlacedOrdersActivity;

public class CustomerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        init();
    }

    private void init() {

        setupToolbar();
    }

    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerHome_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // show menu(home/back button) icon on the left
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_action_menu);
            // don't show default(app_name) title
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    /*
    set "chef_menu" as toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     menu option clicks
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean returnValue = true;

        switch (item.getItemId()){

            case R.id.customer_notification_option:

                break;

            case R.id.customer_search_option:

                break;

            default:
                returnValue = super.onOptionsItemSelected(item);
        }

        return returnValue;
    }
}