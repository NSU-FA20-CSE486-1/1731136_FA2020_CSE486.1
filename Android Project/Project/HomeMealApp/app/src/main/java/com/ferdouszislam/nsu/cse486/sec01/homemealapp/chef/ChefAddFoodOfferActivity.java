package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOfferFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.CapturedImage;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.FileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.firebaseImageUpload.FirebaseStorageFileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.ChefUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.RemoteStoragePathsUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChefAddFoodOfferActivity extends AppCompatActivity {

    private static final String TAG = "CAFOA-debug";

    // request code for camera intent
    protected static final int REQUEST_IMAGE_CAPTURE = 589;

    // auth to access taken image file
    protected static final String FILE_PROVIDER_AUTHORITY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.fileprovider";

    // ui
    protected ImageView foodImageView;
    protected EditText foodNameEditText, priceEditText, descriptionEditText;
    protected EditText itemsEditText, tagsEditText, quantityEditText;
    protected Button offerFoodButton;

    // model
    protected FoodOffer mFoodOffer;

    // model for captured image
    protected CapturedImage mImage;
    protected boolean mImageWasTaken;

    // variables to authenticate user login state, because uid is needed here
    protected Authentication mAuth;
    protected Authentication.AuthenticationCallbacks mAuthenticationCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            mFoodOffer.setmChefUid(user.getmUid());

            authentionSuccessUI();
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(ChefAddFoodOfferActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(ChefAddFoodOfferActivity.this, mAuth);
        }
    };

    // variables to upload photo to firebase storage
    protected FirebaseStorageFileUploader mFileUploader;
    protected FileUploader.FileUploadCallbacks mFileUploadCallbacks = new FileUploader.FileUploadCallbacks() {
        @Override
        public void onUploadComplete(Uri uploadedImageLink) {

            try {

                URL link = new URL(uploadedImageLink.toString());

                mFoodOffer.setmFoodPhotoUrl(link.toString());

                saveFoodOfferToDatabase(mFoodOffer, mFoodOfferDao);

            } catch (MalformedURLException e) {

                Log.d(TAG, "onUploadComplete: image upload error->"+e.getStackTrace());

                commonErrorUI();
            }
        }

        @Override
        public void onUploadFailed(String message) {

            imageUploadFailedUI();

            foodOfferSaveCompleteUI();

            Log.d(TAG, "onUploadFailed: error->" + message);
        }
    };

    // shared preference variable for updating local chef user profile
    protected ChefUserProfileSharedPref mChefUserProfileSharedPref;


    /**
     * store food offer to database
     * @param foodOffer food offere to be stored to database
     * @param foodOfferDao dao for food offer model
     */
    protected void saveFoodOfferToDatabase(FoodOffer foodOffer, FoodOfferDao foodOfferDao) {

        foodOfferDao.createFoodOffer(foodOffer, new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

                saveFoodOfferToDatabaseSuccessUI();
            }

            @Override
            public void onFailed(String failedResponse) {

                foodOfferSaveCompleteUI();
                saveFoodOfferToDatabaseFailureUI();
            }
        });
    }


    // variables to store food offer to database
    protected FoodOfferDao mFoodOfferDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_add_food_offer);

        init();
    }

    protected void init() {

        setupToolbar();

        foodImageView = findViewById(R.id.chef_addFoodOffer_foodOffer_ImageView);
        foodNameEditText = findViewById(R.id.chef_addFoodOffer_foodName_EditText);
        priceEditText = findViewById(R.id.chef_addFoodOffer_price_EditText);
        descriptionEditText = findViewById(R.id.chef_addFoodOffer_description_EditText);
        itemsEditText = findViewById(R.id.chef_addFoodOffer_items_EditText);
        tagsEditText = findViewById(R.id.chef_addFoodOffer_tags_EditText);
        quantityEditText = findViewById(R.id.chef_addFoodOffer_quantity_EditText);
        offerFoodButton = findViewById(R.id.offerFood_Button);

        mFoodOffer = new FoodOffer();

        mFileUploader = new FirebaseStorageFileUploader();
        mImageWasTaken = false;

        mFoodOfferDao = new FoodOfferFirebaseRealtimeDao();

        mChefUserProfileSharedPref = ChefUserProfileSharedPref.build(this);

        mAuth = new FirebaseEmailPasswordAuthentication();
        authenticateUser(mAuth, mAuthenticationCallbacks);
    }

    /*
    authenticate user to get the uid
     */
    protected void authenticateUser(Authentication auth, Authentication.AuthenticationCallbacks authCallbacks) {

        auth.setmAuthenticationCallbacks(authCallbacks);
        auth.authenticateUser();
    }

    /*
    setup the toolbar with back button
     */
    protected void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.chefAddFoodOffer_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.addFoodOffer_title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK) return;

        switch (requestCode){

            case REQUEST_IMAGE_CAPTURE:

                mImageWasTaken = true;

                foodImageView.setImageURI(mImage.getmPhotoUri());

                break;
        }
    }

    /*
    "addFoodPhoto" click listener
     */
    public void addFoodPhotoClick(View view) {

        try {

            // initialize an mImage object only the first time 'addFoodPhoto' button was pressed
            if(mImage==null || !mImageWasTaken) mImage = CapturedImage.build(this);

            dispatchTakePictureIntent(mImage);

        } catch (IOException e) {

            commonErrorUI();

            Log.d(TAG, "takePhotoClick: error->"+e.getMessage());
        }
    }

    /**
     * open default camera app to take an image
     * @param image model for image to be capture, with info of temporarily created local file info
     */
    protected void dispatchTakePictureIntent(CapturedImage image) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = image.getmPhotoFile();

            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(
                        this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile
                );

                image.setmPhotoUri(photoURI);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /*
    "offerFood" button click listener
     */
    public void offerFoodClick(View view) {

        String foodName = foodNameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String items = itemsEditText.getText().toString().trim();
        String tags = tagsEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();

        if(validateInputs(foodName, price, description, items, tags, quantity, mImageWasTaken)){

            mFoodOffer.setmFoodName(foodName);
            mFoodOffer.setmPrice(Integer.parseInt(price));
            mFoodOffer.setmDescription(description);
            mFoodOffer.setmItems(items);
            mFoodOffer.setmTags(tags);
            mFoodOffer.setmQuantity(quantity);

            // load address and region from chef user profile information
            mFoodOffer.setmAddress(mChefUserProfileSharedPref.getChefUser().getmHomeAddress());
            mFoodOffer.setmRegion(mChefUserProfileSharedPref.getChefUser().getmRegion());

            foodOfferSaveInProgressUI();

            saveFoodOffer();
        }
    }

    /*
    validate user inputs
     */
    protected boolean validateInputs(String foodName, String price, String description, String items,
                                   String tags, String quantity, boolean imageTaken) {

        boolean isValid = true;

        if(!InputValidatorUtil.isFoodNameValid(foodName)){
            foodNameEditText.setError(getString(R.string.food_name_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodPriceValid(price)){
            priceEditText.setError(getString(R.string.food_price_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodDescriptionValid(description)){
            descriptionEditText.setError(getString(R.string.food_description_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodItemsValid(items)){
            itemsEditText.setError(getString(R.string.food_items_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodTagsValid(tags)){
            tagsEditText.setError(getString(R.string.food_tags_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodQuantityValid(quantity)){
            quantityEditText.setError(getString(R.string.food_quantity_error));
            isValid = false;
        }

        if(!imageTaken){

            Toast.makeText(this, R.string.add_food_photo_please, Toast.LENGTH_SHORT)
                    .show();
            isValid = false;
        }

        return isValid;
    }

    /*
    start the food offer info uploading process
     */
    protected void saveFoodOffer() {

        // start the image uploading,
        // once the image uploading is done 'mFoodOffer' will be saved to database
        uploadImage(mFileUploader, mFileUploadCallbacks, mImage);
    }

    /**
     * upload captured food image to remote data storage
     * @param fileUploader file uploader object
     * @param image object for image to be uploaded
     */
    protected void uploadImage(FirebaseStorageFileUploader fileUploader, FileUploader.FileUploadCallbacks fileUploadCallbacks,
                             CapturedImage image){

        fileUploader.uploadFile(
                image,
                RemoteStoragePathsUtil.FOOD_OFFER_IMAGE_NODE + "/" + image.getmPhotoFileName(),
                fileUploadCallbacks
        );
    }


    /*
    UI event for when authentication is undergoing
     */
    protected void authenticatingUI(){

        offerFoodButton.setEnabled(false);
    }

    /*
    UI event for when authentication is undergoing
     */
    protected void authentionSuccessUI(){

        offerFoodButton.setEnabled(true);
    }

    /*
    UI event for common errors
     */
    protected void commonErrorUI() {

        Toast.makeText(this, R.string.an_unexpected_error_occurred, Toast.LENGTH_SHORT)
                .show();
    }

    /*
    UI event for image upload failed
     */
    protected void imageUploadFailedUI(){

        Toast.makeText(this, R.string.image_upload_failed, Toast.LENGTH_SHORT)
                .show();
    }

    /*
    UI event for while waiting for food offer to get saved
     */
    protected void foodOfferSaveInProgressUI(){

        offerFoodButton.setText(getString(R.string.saving));
        offerFoodButton.setEnabled(false);
    }

    /*
    UI event for when food offer saving process is complete (success or failed)
     */
    protected void foodOfferSaveCompleteUI(){

        offerFoodButton.setText(getString(R.string.offerFood_ButtonLabel));
        offerFoodButton.setEnabled(true);
    }

    /*
    UI event for food offer save to database success
     */
    protected void saveFoodOfferToDatabaseSuccessUI(){

        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
                .show();

        finish();
    }

    /*
    UI event for food offer save to database failure
     */
    protected void saveFoodOfferToDatabaseFailureUI(){

        Toast.makeText(this, R.string.food_offer_save_failed, Toast.LENGTH_SHORT)
                .show();
    }
}