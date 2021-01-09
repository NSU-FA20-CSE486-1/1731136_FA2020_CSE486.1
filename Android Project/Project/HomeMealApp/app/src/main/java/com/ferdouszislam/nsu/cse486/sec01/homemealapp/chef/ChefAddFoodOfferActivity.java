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
import android.widget.ImageView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.CapturedImage;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.FileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.firebaseImageUpload.FirebaseStorageFileUploader;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.RemoteStoragePathsUtil;

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

    // model for captured image
    private CapturedImage mImage;
    private boolean mImageWasTaken;

    // variables to upload photo to firebase storage
    private FirebaseStorageFileUploader mFileUploader;
    private FileUploader.FileUploadCallbacks<Uri> mFileUploadCallbacks = new FileUploader.FileUploadCallbacks<Uri>() {
        @Override
        public void onUploadComplete(Uri uploadedImageLink) {

            try {

                URL link = new URL(uploadedImageLink.toString());

            } catch (MalformedURLException e) {

                Log.d(TAG, "onUploadComplete: image upload error->"+e.getStackTrace());

                commonErrorUI();
            }
        }

        @Override
        public void onUploadFailed(String message) {

            imageUploadFailedUI();

            Log.d(TAG, "onUploadFailed: error->" + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_add_food_offer);

        init();
    }

    private void init() {

        setupToolbar();

        mFoodImageView = findViewById(R.id.chef_addFoodOffer_foodOffer_ImageView);

        mFileUploader = new FirebaseStorageFileUploader(mFileUploadCallbacks);
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

                // TODO: upload after user presses 'Offer' button, not here
                uploadImage(mFileUploader, mImage);

                break;
        }
    }

    /**
     * upload captured food image to remote data storage
     * @param fileUploader file uploader object
     * @param image object for image to be uploaded
     */
    private void uploadImage(FirebaseStorageFileUploader fileUploader, CapturedImage image){

        fileUploader.uploadFile(image, RemoteStoragePathsUtil.FOOD_OFFER_IMAGE_NODE + "/" + image.getmPhotoFileName());
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
}