package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;

public class ChefCreateFoodVariantActivity extends ChefAddFoodOfferActivity {

    private static final String TAG = "CCFVA-debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //use the same layout as "Add Food Offering"
        setContentView(R.layout.activity_chef_add_food_offer);

        init();

        mFoodOffer = (FoodOffer) getIntent().getSerializableExtra(ChefHomeActivity.FOOD_VARIANT_KEY);

        populateInputFields(mFoodOffer);
    }


    /**
     * populate the input fields with entries from the
     * food item whose variant is being created
     * (the data should be passed from previous Activity)
     */
    private void populateInputFields(FoodOffer foodOffer) {

        foodNameEditText.setText(foodOffer.getmFoodName());

        descriptionEditText.setText(foodOffer.getmDescription());

        itemsEditText.setText(foodOffer.getmItems());

        priceEditText.setText(foodOffer.getmPrice());

        quantityEditText.setText(foodOffer.getmQuantity());

        tagsEditText.setText(foodOffer.getmTags());
    }


    /*
    setup the toolbar with back button
     */
    @Override
    protected void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.chefAddFoodOffer_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.create_variant_title);
        }
    }
}