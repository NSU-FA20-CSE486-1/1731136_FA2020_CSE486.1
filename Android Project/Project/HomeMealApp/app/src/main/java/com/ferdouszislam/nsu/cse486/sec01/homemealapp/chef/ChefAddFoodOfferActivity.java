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
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos.FoodOfferFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.CapturedImage;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.FileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.firebaseImageUpload.FirebaseStorageFileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
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
    private static final int REQUEST_IMAGE_CAPTURE = 589;

    // auth to access taken image file
    private static final String FILE_PROVIDER_AUTHORITY = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.fileprovider";

    // ui
    private ImageView mFoodImageView;
    private EditText mFoodNameEditText, mPriceEditText, mDescriptionEditText;
    private EditText mItemsEditText, mTagsEditText, mQuantityEditText;
    private Button mOfferFoodButton;

    // model
    private FoodOffer mFoodOffer;

    // model for captured image
    private CapturedImage mImage;
    private boolean mImageWasTaken;

    // variables to authenticate user login state, because uid is needed here
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthenticationCallbacks = new Authentication.AuthenticationCallbacks() {
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
    private FirebaseStorageFileUploader mFileUploader;
    private FileUploader.FileUploadCallbacks mFileUploadCallbacks = new FileUploader.FileUploadCallbacks() {
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

    /**
     * store food offer to database
     * @param foodOffer food offere to be stored to database
     * @param foodOfferDao dao for food offer model
     */
    private void saveFoodOfferToDatabase(FoodOffer foodOffer, FoodOfferDao foodOfferDao) {

        foodOfferDao.createFoodOfferForChef(foodOffer, foodOffer.getmChefUid(), new DatabaseOperationStatusListener<Void, String>() {
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
    private FoodOfferDao mFoodOfferDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_add_food_offer);

        init();
    }

    private void init() {

        setupToolbar();

        mFoodImageView = findViewById(R.id.chef_addFoodOffer_foodOffer_ImageView);
        mFoodNameEditText = findViewById(R.id.chef_addFoodOffer_foodName_EditText);
        mPriceEditText = findViewById(R.id.chef_addFoodOffer_price_EditText);
        mDescriptionEditText = findViewById(R.id.chef_addFoodOffer_description_EditText);
        mItemsEditText = findViewById(R.id.chef_addFoodOffer_items_EditText);
        mTagsEditText = findViewById(R.id.chef_addFoodOffer_tags_EditText);
        mQuantityEditText = findViewById(R.id.chef_addFoodOffer_quantity_EditText);
        mOfferFoodButton = findViewById(R.id.offerFood_Button);

        mFoodOffer = new FoodOffer();

        mFileUploader = new FirebaseStorageFileUploader();

        mFoodOfferDao = new FoodOfferFirebaseRealtimeDao();

        mAuth = new FirebaseEmailPasswordAuthentication();
        authenticateUser(mAuth, mAuthenticationCallbacks);
    }

    /*
    authenticate user to get the uid
     */
    private void authenticateUser(Authentication auth, Authentication.AuthenticationCallbacks authCallbacks) {

        auth.setmAuthenticationCallbacks(authCallbacks);
        auth.authenticateUser();
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

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

                mFoodImageView.setImageURI(mImage.getmPhotoUri());

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
    private void dispatchTakePictureIntent(CapturedImage image) {

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

        String foodName = mFoodNameEditText.getText().toString().trim();
        String price = mPriceEditText.getText().toString().trim();
        String description = mDescriptionEditText.getText().toString().trim();
        String items = mItemsEditText.getText().toString().trim();
        String tags = mTagsEditText.getText().toString().trim();
        String quantity = mQuantityEditText.getText().toString().trim();

        if(validateInputs(foodName, price, description, items, tags, quantity, mImageWasTaken)){

            mFoodOffer.setmFoodName(foodName);
            mFoodOffer.setmPrice(price);
            mFoodOffer.setmDescription(description);
            mFoodOffer.setmItems(items);
            mFoodOffer.setmTags(tags);
            mFoodOffer.setmQuantity(quantity);

            foodOfferSaveInProgressUI();

            saveFoodOffer();
        }
    }

    /*
    validate user inputs
     */
    private boolean validateInputs(String foodName, String price, String description, String items,
                                   String tags, String quantity, boolean imageTaken) {

        boolean isValid = true;

        if(!InputValidatorUtil.isFoodNameValid(foodName)){
            mFoodNameEditText.setError(getString(R.string.food_name_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodPriceValid(price)){
            mPriceEditText.setError(getString(R.string.food_price_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodDescriptionValid(description)){
            mDescriptionEditText.setError(getString(R.string.food_description_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodItemsValid(items)){
            mItemsEditText.setError(getString(R.string.food_items_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodTagsValid(tags)){
            mTagsEditText.setError(getString(R.string.food_tags_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isFoodQuantityValid(quantity)){
            mQuantityEditText.setError(getString(R.string.food_quantity_error));
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
    private void saveFoodOffer() {

        // start the image uploading,
        // once the image uploading is done 'mFoodOffer' will be saved to database
        uploadImage(mFileUploader, mFileUploadCallbacks, mImage);
    }

    /**
     * upload captured food image to remote data storage
     * @param fileUploader file uploader object
     * @param image object for image to be uploaded
     */
    private void uploadImage(FirebaseStorageFileUploader fileUploader, FileUploader.FileUploadCallbacks fileUploadCallbacks,
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
    private void authenticatingUI(){

        mOfferFoodButton.setEnabled(false);
    }

    /*
    UI event for when authentication is undergoing
     */
    private void authentionSuccessUI(){

        mOfferFoodButton.setEnabled(true);
    }

    /*
    UI event for common errors
     */
    private void commonErrorUI() {

        Toast.makeText(this, R.string.an_unexpected_error_occurred, Toast.LENGTH_SHORT)
                .show();
    }

    /*
    UI event for image upload failed
     */
    private void imageUploadFailedUI(){

        Toast.makeText(this, R.string.image_upload_failed, Toast.LENGTH_SHORT)
                .show();
    }

    /*
    UI event for while waiting for food offer to get saved
     */
    private void foodOfferSaveInProgressUI(){

        mOfferFoodButton.setText(getString(R.string.saving));
        mOfferFoodButton.setEnabled(false);
    }

    /*
    UI event for when food offer saving process is complete (success or failed)
     */
    private void foodOfferSaveCompleteUI(){

        mOfferFoodButton.setText(getString(R.string.offerFood_ButtonLabel));
        mOfferFoodButton.setEnabled(true);
    }

    /*
    UI event for food offer save to database success
     */
    private void saveFoodOfferToDatabaseSuccessUI(){

        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
                .show();

        finish();
    }

    /*
    UI event for food offer save to database failure
     */
    private void saveFoodOfferToDatabaseFailureUI(){

        Toast.makeText(this, R.string.food_offer_save_failed, Toast.LENGTH_SHORT)
                .show();
    }
}