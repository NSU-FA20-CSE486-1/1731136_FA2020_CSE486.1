package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

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
            // don't show default(app_name) title
            ab.setTitle("Home");
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

            case R.id.customer_menu_option:

                startActivity(new Intent(this, CustomerMenuActivity.class));
                break;

            case R.id.customer_search_option:

                findViewById(R.id.searchFilter_View).setVisibility(View.VISIBLE);
                break;

            default:
                returnValue = super.onOptionsItemSelected(item);
        }

        return returnValue;
    }

    /*
    search filter close click listener
     */
    public void closeSearchFilterClick(View view) {

        findViewById(R.id.searchFilter_View).setVisibility(View.GONE);
    }
}