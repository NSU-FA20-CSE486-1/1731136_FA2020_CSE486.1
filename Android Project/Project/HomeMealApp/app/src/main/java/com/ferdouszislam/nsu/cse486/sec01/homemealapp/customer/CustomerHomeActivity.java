package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters.FoodOffersAdapter;

public class CustomerHomeActivity extends AppCompatActivity implements FoodOffersAdapter.CallerCallback{

    public static final String FOOD_OFFER_DETAILS_KEY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer-foodOfferDetailsKey";

    // ui
    private TextView mNoFoodOffersTextView;
    private RecyclerView mFoodOffersRecyclerView;
    private FoodOffersAdapter mFoodOffersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        init();
    }

    private void init() {

        setupToolbar();

        mNoFoodOffersTextView = findViewById(R.id.noFoodOffersFound_TextView);
        mFoodOffersRecyclerView = findViewById(R.id.customerHome_foodOffers_RecyclerView);

        mFoodOffersAdapter = new FoodOffersAdapter(this, this);
        mFoodOffersRecyclerView.setAdapter(mFoodOffersAdapter);
        mFoodOffersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void onSeeMoreClick(FoodOffer foodOffer) {

        Intent intent = new Intent(this, CustomerFoodOfferDetailsActivity.class);

        intent.putExtra(FOOD_OFFER_DETAILS_KEY, foodOffer);

        startActivity(intent);
    }

    @Override
    public void onFoodOffersListNotEmpty() {

        mNoFoodOffersTextView.setVisibility(View.GONE);
        mFoodOffersRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedToLoadFoodOffers() {

        Toast.makeText(this, R.string.an_unexpected_error_occurred, Toast.LENGTH_LONG)
                .show();
    }
}