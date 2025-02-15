package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.commonActivities.BaseActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.recyclerViewAdapters.ChefFoodOffersAdapter;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.ChefUserProfileSharedPref;

public class ChefHomeActivity extends BaseActivity implements ChefFoodOffersAdapter.CallerCallback {

    public static final String FOOD_VARIANT_KEY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef-createVariant";

    // ui
    private TextView mNoFoodOffersTextView;
    private RecyclerView mFoodOffersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        enableInternetStatusFeedback(findViewById(R.id.chef_home_main_view));

        init();

        stopRunningServiceAndStartNewServiceIfUserEnabled();
    }

    /*
    stop already running notification service if running
    start a new service if user has enabled in settings
     */
    private void stopRunningServiceAndStartNewServiceIfUserEnabled() {

        boolean notificationWasEnabled =
                PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.notification_switch_preference_key), false);

        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);

        if(notificationWasEnabled) startService(intent);
    }

    private void init() {

        setupToolbar();

        mNoFoodOffersTextView = findViewById(R.id.chefHome_noFoodOffers_TextView);

        mFoodOffersRecyclerView = findViewById(R.id.chefHome_foodOffers_RecyclerView);

        ChefFoodOffersAdapter adapter = new ChefFoodOffersAdapter(
                this,
                this,
                ChefUserProfileSharedPref.build(this).getChefUser().getmUid());
        mFoodOffersRecyclerView.setAdapter(adapter);
        mFoodOffersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

            case R.id.chef_notification_option:

                startActivity(new Intent(this, ChefPlacedOrdersActivity.class));
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

    @Override
    public void onCreateVariantClick(FoodOffer foodOffer) {

        Intent intent = new Intent(this, ChefCreateFoodVariantActivity.class);

        intent.putExtra(FOOD_VARIANT_KEY, foodOffer);

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

    @Override
    public void onFailedToDeleteFoodOffer() {

        Toast.makeText(this, R.string.failed_to_delete_food_offer, Toast.LENGTH_LONG)
                .show();
    }
}