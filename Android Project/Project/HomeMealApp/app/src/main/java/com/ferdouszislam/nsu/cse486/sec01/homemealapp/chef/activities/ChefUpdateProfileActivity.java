package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.ChefUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class ChefUpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "CUPA-debug";

    // ui
    private EditText mPhoneNumberEditText, mHomeAddressEditText;
    private Button mUpdateButton;
    private Spinner mRegionSpinner;

    // model
    private ChefUser mChefUser;

    // variable to read and update in database
    private ChefUserDao mChefUserDao;
    private SingleDataChangeListener<ChefUser> mSingleDataChangeListener = new SingleDataChangeListener<ChefUser>() {
        @Override
        public void onDataChange(ChefUser data) {

            mChefUser = data;

            mChefUserProfileSharedPref.setChefUser(mChefUser);

            populateInputFields(mChefUser);
        }
    };

    // variables for user authentication
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            mChefUserDao = new ChefUserFirebaseRealtimeDao();
            loadChefUserDataFromDatabase(user.getmUid(), mChefUserDao, mSingleDataChangeListener);
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(ChefUpdateProfileActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(ChefUpdateProfileActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };


    // shared preference variable for updating local chef user profile
    private ChefUserProfileSharedPref mChefUserProfileSharedPref;


    /**
     * read user existing information from database
     * @param id user id to search
     * @param chefUserDao dao object
     */
    private void loadChefUserDataFromDatabase(String id, ChefUserDao chefUserDao,
                                              SingleDataChangeListener<ChefUser> singleDataChangeListener) {

        chefUserDao.readWithId(
                id,

                singleDataChangeListener,

                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {

                        progressCompleteUI();
                    }

                    @Override
                    public void onFailed(String failedResponse) {

                        failedToPerformDatabaseActionUI();

                        Log.d(TAG, "onFailed: user data read error -> "+failedResponse);
                    }
                }
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use the same layout as "Chef Sign-up"
        setContentView(R.layout.activity_chef_signup);

        init();
    }

    private void init() {

        setupToolbar();

        setupSpinner();

        mPhoneNumberEditText = findViewById(R.id.chefSignup_PhoneNumber_EditText);
        mHomeAddressEditText = findViewById(R.id.chefSignup_address_EditText);

        mUpdateButton = findViewById(R.id.chefSignup_Button);
        mUpdateButton.setText(getString(R.string.save));
        mUpdateButton.setOnClickListener(v -> updateButtonClick());

        hideUnnecessaryInputFields();

        inProgressUI();

        mAuth = new FirebaseEmailPasswordAuthentication();
        authenticateUser(mAuth, mAuthCallbacks);

        mChefUserProfileSharedPref = ChefUserProfileSharedPref.build(this);
    }

    private void hideUnnecessaryInputFields() {

        findViewById(R.id.chefSignupWelcome_TextView).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_email_EditText).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_password_EditText).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_confirmPassword_EditText).setVisibility(View.GONE);
        findViewById(R.id.chefSignUpPasswordHint_TextView).setVisibility(View.GONE);
    }

    /**
     * authenticate if user is logged in
     * @param auth authenticator object
     * @param authCallbacks authentication status callbacks
     */
    private void authenticateUser(Authentication auth, Authentication.AuthenticationCallbacks authCallbacks) {

        if(auth==null) auth = new FirebaseEmailPasswordAuthentication();

        auth.setmAuthenticationCallbacks(authCallbacks);
        auth.authenticateUser();
    }


    /**
     * fill input fields with existing user data
     * @param chefUser existing chef user
     */
    private void populateInputFields(ChefUser chefUser) {

        mPhoneNumberEditText.setText(chefUser.getmPhoneNumber());
        mHomeAddressEditText.setText(chefUser.getmHomeAddress());

        int selectedItemPostion = 0;
        String[] regions = getResources().getStringArray(R.array.regions_array);
        for(int i=0; i<regions.length; i++){
            if(regions[i].equals(chefUser.getmRegion())) {
                selectedItemPostion = i;
                break;
            }
        }
        mRegionSpinner.setSelection(selectedItemPostion);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.chefSignup_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.update_profile_title);
        }
    }

    /*
    setup the region selector spinner
     */
    private void setupSpinner() {

        mRegionSpinner = findViewById(R.id.chefSignup_region_Spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, R.layout.region_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.region_spinner_dropdown);
        // Apply the adapter to the spinner
        mRegionSpinner.setAdapter(adapter);
    }


    /*
    "save" button click listener
     */
    private void updateButtonClick() {

        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String homeAddress = mHomeAddressEditText.getText().toString().trim();
        String region = mRegionSpinner.getSelectedItem()+"";

        if(validateInputs(phoneNumber, homeAddress) && dataWasUpdated(phoneNumber, homeAddress, region)){

            mChefUser.setmPhoneNumber(phoneNumber);
            mChefUser.setmHomeAddress(homeAddress);
            mChefUser.setmRegion(region);

            inProgressUI();

            updateChefUserProfile(mChefUser, mChefUserDao, mChefUserProfileSharedPref);
        }
    }

    /**
     * update chef user's profile data
     * @param chefUser updated chef user object
     * @param chefUserDao dao object
     * @param chefUserProfileSharedPref shared pref object for chef user profile info
     */
    private void updateChefUserProfile(ChefUser chefUser, ChefUserDao chefUserDao,
                                       ChefUserProfileSharedPref chefUserProfileSharedPref) {

        chefUserDao.updateWithId(chefUser, chefUser.getmUid(), new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

                chefUserProfileSharedPref.setChefUser(chefUser);

                progressCompleteUI();
                updateSuccessUI();
            }

            @Override
            public void onFailed(String failedResponse) {

                progressCompleteUI();
                failedToPerformDatabaseActionUI();
            }
        });
    }


    /**
     * validate user inputs and show UI for invalid ones
     * @param phoneNumber updated phone number
     * @param homeAddress update home address
     * @return if inputs are valid
     */
    private boolean validateInputs(String phoneNumber, String homeAddress) {

        boolean isValid = true;

        if(!InputValidatorUtil.isValidPhoneNumber(phoneNumber)){

            mPhoneNumberEditText.setError(getString(R.string.phone_input_error));
            isValid = false;
        }

        if(!InputValidatorUtil.isValidHomeAddress(homeAddress)){

            mHomeAddressEditText.setError(getString(R.string.address_input_error));
            isValid = false;
        }

        return isValid;
    }

    /**
     * check whether data was updated or not
     * @param phoneNumber phone number input
     * @param homeAddress home address input
     * @return if data was changed or not
     */
    private boolean dataWasUpdated(String phoneNumber, String homeAddress, String region) {

        return !mChefUser.getmPhoneNumber().equals(phoneNumber)
                || !mChefUser.getmHomeAddress().equals(homeAddress)
                || !mChefUser.getmRegion().equals(region);
    }

    /*
    UI to show user while registration is in progress
     */
    private void inProgressUI() {

        mUpdateButton.setText(getString(R.string.loading));
        mUpdateButton.setEnabled(false);
    }

    /*
    authentication/database upload progress finished UI event
     */
    private void progressCompleteUI() {

        mUpdateButton.setText(getString(R.string.save));
        mUpdateButton.setEnabled(true);
    }

    /*
    UI event for update/download failure
     */
    private void failedToPerformDatabaseActionUI() {

        Toast.makeText(this, R.string.data_load_error, Toast.LENGTH_SHORT)
                .show();
    }

    /*
    UI event for update success
     */
    private void updateSuccessUI() {

        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
                .show();
    }
}