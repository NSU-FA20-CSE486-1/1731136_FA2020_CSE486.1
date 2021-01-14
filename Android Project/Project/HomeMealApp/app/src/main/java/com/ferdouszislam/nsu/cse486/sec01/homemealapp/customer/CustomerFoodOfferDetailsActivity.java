package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;

public class CustomerFoodOfferDetailsActivity extends AppCompatActivity {

    public static final String FOOD_OFFER_ORDER_KEY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer-foodOfferOrderKey";

    // ui
    private ImageView mFoodPhotoImageView;
    private TextView mFoodNameTextView, mLocationTextView, mPriceTextView;
    private TextView mDescriptionTextView, mItemsTextView, mTagsTextView, mQuantityTextView;
    private Button mContactVendorButton;

    // model
    private FoodOffer mFoodOffer;

    // variables to fetch vendor contact number
    private ChefUserDao mChefUserDao;
    private SingleDataChangeListener<ChefUser> mChefUserSingleDataChangeListener =
            chefUser -> dialPhoneNumber(chefUser.getmPhoneNumber());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_offer_details);

        init();
    }

    private void init() {

        setupToolbar();

        mFoodPhotoImageView = findViewById(R.id.customer_food_details_foodImageView);
        mFoodNameTextView = findViewById(R.id.customer_food_details_foodName_TextView);
        mLocationTextView = findViewById(R.id.customer_food_details_location_TextView);
        mPriceTextView = findViewById(R.id.customer_food_details_price_TextView);
        mDescriptionTextView = findViewById(R.id.customer_food_details_DescriptionContent_TextView);
        mItemsTextView = findViewById(R.id.customer_food_details_ItemsContent_TextView);
        mTagsTextView = findViewById(R.id.customer_food_details_TagsContent_TextView);
        mQuantityTextView = findViewById(R.id.customer_food_details_QuantityContent_TextView);
        mContactVendorButton  = findViewById(R.id.customer_food_details_contact_button);

        mFoodOffer = (FoodOffer) getIntent().getSerializableExtra(CustomerHomeActivity.FOOD_OFFER_DETAILS_KEY);

        populateFoodOfferDetailFields(mFoodOffer);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerFoodOfferDetails_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.meal_details);
        }
    }

    /**
     * populate view with food offer details
     * @param foodOffer food offer model object
     */
    private void populateFoodOfferDetailFields(FoodOffer foodOffer) {

        Glide.with(this)
                .load(foodOffer.getmFoodPhotoUrl())
                .placeholder(R.drawable.ic_action_loading)
                .override(300, 300)
                .fitCenter() // scale to fit entire image within ImageView
                .into(mFoodPhotoImageView);

        mFoodNameTextView.setText(foodOffer.getmFoodName());
        String location = foodOffer.getmRegion() + " ("+foodOffer.getmAddress()+")";
        mLocationTextView.setText(location);
        String price = foodOffer.getmPrice() + " TK";
        mPriceTextView.setText(price);
        mDescriptionTextView.setText(foodOffer.getmDescription());
        mItemsTextView.setText(foodOffer.getmItems());
        mTagsTextView.setText(foodOffer.getmTags());
        mQuantityTextView.setText(foodOffer.getmQuantity());
    }


    // 'Contact Vendor' button click listener
    public void contactVendorClick(View view) {

        mChefUserDao = new ChefUserFirebaseRealtimeDao();
        mChefUserDao.readWithId(
                mFoodOffer.getmChefUid(),
                mChefUserSingleDataChangeListener,
                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {
                        // kept blank intentionally
                    }

                    @Override
                    public void onFailed(String failedResponse) {
                        contactVendorFailedUI();
                    }
                });

        contactVendorInProgressUI();
    }

    /**
     * open default phone app to call vendor
     * @param phoneNumber phone number to call
     */
    private void dialPhoneNumber(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            contactVendorSuccessUI();
        }
        else contactVendorFailedUI();
    }

    // 'Place Order' button click listener
    public void placeOrderClick(View view) {
        Intent intent = new Intent(this, CustomerPlaceOrderActivity.class);
        intent.putExtra(FOOD_OFFER_ORDER_KEY, mFoodOffer);
        startActivity(intent);
        finish();
    }


    private void contactVendorInProgressUI(){

        mContactVendorButton.setEnabled(false);
        mContactVendorButton.setText(getString(R.string.contacting));
    }

    private void contactVendorSuccessUI(){
        mContactVendorButton.setEnabled(true);
        mContactVendorButton.setText(getString(R.string.customer_food_details_contact_button_label));
    }

    private void contactVendorFailedUI(){

        mContactVendorButton.setEnabled(true);
        mContactVendorButton.setText(getString(R.string.customer_food_details_contact_button_label));

        Toast.makeText(this, R.string.failed_to_contact, Toast.LENGTH_SHORT)
                .show();
    }
}