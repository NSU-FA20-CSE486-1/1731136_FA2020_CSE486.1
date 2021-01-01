package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;

public class ChefHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        init();
    }

    private void init() {

        setupToolbar();
    }

    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.chefHome_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // don't show default(app_name) title
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    /*
    set "chef_menu" as toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.chef_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     menu option clicks
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean returnValue = true;

        switch (item.getItemId()){

            case R.id.chef_menu_option:

                startActivity(new Intent(this, ChefMenuActivity.class));
                break;

            case R.id.notificationOption:


                break;

            default:
                returnValue = super.onOptionsItemSelected(item);
        }

        return returnValue;
    }

    /*
    "addFoodItem_Button" click listener
     */
    public void addFoodItemClick(View view) {

        startActivity(new Intent(this, ChefAddFoodOfferActivity.class));
    }
}