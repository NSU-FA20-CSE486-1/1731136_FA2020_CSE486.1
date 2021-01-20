package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.commonActivities.BaseActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters.CustomerPlacedOrdersAdapter;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class CustomerPlacedOrdersActivity extends BaseActivity implements CustomerPlacedOrdersAdapter.CallerCallback {

    private static final String TAG = "CustPOA-debug";

    public static final String FOOD_ORDER_KEY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer-foodOrderKey";

    // ui
    private TextView mNoOrdersPlacedTextView;
    private RecyclerView mPlacedOrdersRecyclerView;
    private CustomerPlacedOrdersAdapter mPlacedOrdersAdapter;

    // model data
    private String mCustomerUid;

    // variables for user authentication
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            mCustomerUid = user.getmUid();
            loadRecyclerView(mCustomerUid);
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(CustomerPlacedOrdersActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(CustomerPlacedOrdersActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_placed_orders);

        enableInternetStatusFeedback(findViewById(R.id.customer_placed_orders_main_view));

        init();
    }

    private void init() {

        setupToolbar();

        mNoOrdersPlacedTextView = findViewById(R.id.customer_noPlacedOrders_TextView);
        mPlacedOrdersRecyclerView = findViewById(R.id.customerPlacedOrders_RecyclerView);

        mAuth = new FirebaseEmailPasswordAuthentication(mAuthCallbacks);
        mAuth.authenticateUser();
    }

    private void loadRecyclerView(String mCustomerUid) {

        mPlacedOrdersAdapter = new CustomerPlacedOrdersAdapter(mCustomerUid, this);
        mPlacedOrdersRecyclerView.setAdapter(mPlacedOrdersAdapter);
        mPlacedOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerPlacedOrders_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.placed_orders_title);
        }
    }

    @Override
    public void onPayNowClick(FoodOrder foodOrder) {
        Intent intent = new Intent(this, CustomerPaymentActivity.class);
        intent.putExtra(FOOD_ORDER_KEY, foodOrder);
        startActivity(intent);
    }

    @Override
    public void contactVendorClick(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else contactVendorFailedUI();
    }

    @Override
    public void onPlacedOrdersListNotEmpty() {

        mNoOrdersPlacedTextView.setVisibility(View.GONE);
        mPlacedOrdersRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedToLoadPlacedOrders() {

        Toast.makeText(this, R.string.an_unexpected_error_occurred, Toast.LENGTH_SHORT)
                .show();
    }

    private void contactVendorFailedUI() {

        Toast.makeText(this, R.string.call_failed, Toast.LENGTH_SHORT)
                .show();
    }
}