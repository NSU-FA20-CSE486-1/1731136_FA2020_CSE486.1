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

import java.io.File;
import java.io.IOException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_add_food_offer);

        init();
    }

    private void init() {

        setupToolbar();

        mFoodImageView = findViewById(R.id.chef_addFoodOffer_foodOffer_ImageView);

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

    public void addFoodPhotoClick(View view) {

        try {

            mImage = CapturedImage.build(this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK) return;

        switch (requestCode){

            case REQUEST_IMAGE_CAPTURE:

                mImageWasTaken = true;

                mFoodImageView.setImageURI(mImage.getmPhotoUri());

                // TODO: remove toast
                Toast.makeText(this, "image taken!", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    /*
    UI event for common errors
     */
    private void commonErrorUI() {

        Toast.makeText(this, R.string.an_unexpected_error_occurred, Toast.LENGTH_SHORT)
                .show();
    }

    public void offerFoodClick(View view) {
    }
}