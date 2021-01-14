package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;

public class CustomerFoodOfferDetailsActivity extends AppCompatActivity {

    // ui
    private ImageView mFoodPhotoImageView;
    private TextView mFoodNameTextView, mLocationTextView, mPriceTextView;
    private TextView mDescriptionTextView, mItemsTextView, mTagsTextView, mQuantityTextView;
    private Button mContactVendorButton;

    // model
    private FoodOffer mFoodOffer;

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


    // 'Contact Vendor' button click listener
    public void contactVendorClick(View view) {
    }

    // 'Place Order' button click listener
    public void placeOrderClick(View view) {
    }
}