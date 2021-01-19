package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.BaseActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters.FoodOffersAdapter;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;

public class CustomerHomeActivity extends BaseActivity implements FoodOffersAdapter.CallerCallback{

    private static final String TAG = "CustHA-debug";

    public static final String FOOD_OFFER_DETAILS_KEY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer-foodOfferDetailsKey";

    // ui
    private TextView mNoFoodOffersTextView;
    private RecyclerView mFoodOffersRecyclerView;
    private FoodOffersAdapter mFoodOffersAdapter;
    private Spinner mRegionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        enableInternetStatusFeedback(findViewById(R.id.customer_home_main_view));

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

        // TODO: remove added log for tracing notification service bug
        Log.d(TAG, "stopRunningServiceAndStartNewServiceIfUserEnabled: user type -> "+ UserAuthSharedPref.build(this).getUserType());

        if(notificationWasEnabled){
            Intent intent = new Intent(this, NotificationService.class);
            stopService(intent);
            startService(intent);
        }
    }

    private void init() {

        setupToolbar();

        setupSpinner();

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
    setup the region selector spinner
     */
    private void setupSpinner() {

        mRegionSpinner = findViewById(R.id.customerHome_regionFilter_Spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, R.layout.region_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.region_spinner_dropdown);
        // Apply the adapter to the spinner
        mRegionSpinner.setAdapter(adapter);

        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(mFoodOffersAdapter!=null) mFoodOffersAdapter.filterByRegion(parent.getSelectedItem().toString().trim());

                Log.d(TAG, "onItemSelected: selected region = " + parent.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

        if(mFoodOffersAdapter!=null) mFoodOffersAdapter.removeFilter();

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