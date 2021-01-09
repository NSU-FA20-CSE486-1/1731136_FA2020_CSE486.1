package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class ChefUpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "CUPA-debug";

    // ui
    private EditText mPhoneNumberEditText, mHomeAddressEditText;
    private Button mUpdateButton;

    // model
    private ChefUser mChefUser;

    // variable to read and update in database
    private ChefUserDao mChefUserDao;
    private SingleDataChangeListener<ChefUser> mSingleDataChangeListener = new SingleDataChangeListener<ChefUser>() {
        @Override
        public void onDataChange(ChefUser data) {

            mChefUser = data;
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

            SessionUtil.doHardLogout(ChefUpdateProfileActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };


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

        mPhoneNumberEditText = findViewById(R.id.chefSignup_PhoneNumber_EditText);
        mHomeAddressEditText = findViewById(R.id.chefSignup_address_EditText);

        mUpdateButton = findViewById(R.id.chefSignup_Button);
        mUpdateButton.setText(getString(R.string.save));
        mUpdateButton.setOnClickListener(v -> updateButtonClick());

        hideUnnecessaryInputFields();

        inProgressUI();

        authenticateUser(mAuth, mAuthCallbacks);
    }

    private void hideUnnecessaryInputFields() {

        findViewById(R.id.chefSignupWelcome_TextView).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_email_EditText).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_password_EditText).setVisibility(View.GONE);
        findViewById(R.id.chefSignup_confirmPassword_EditText).setVisibility(View.GONE);
        findViewById(R.id.passwordHint_TextView).setVisibility(View.GONE);
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
    "save" button click listener
     */
    private void updateButtonClick() {

        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String homeAddress = mHomeAddressEditText.getText().toString().trim();

        if(validateInputs(phoneNumber, homeAddress) && dataWasUpdated(phoneNumber, homeAddress)){

            mChefUser.setmPhoneNumber(phoneNumber);
            mChefUser.setmHomeAddress(homeAddress);

            inProgressUI();

            updateUserDataInDatabase(mChefUser, mChefUserDao);
        }
    }

    /**
     * update chef user's data in database
     * @param chefUser updated chef user object
     * @param chefUserDao dao object
     */
    private void updateUserDataInDatabase(ChefUser chefUser, ChefUserDao chefUserDao) {

        chefUserDao.updateWithId(chefUser, chefUser.getmUid(), new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

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
    private boolean dataWasUpdated(String phoneNumber, String homeAddress) {

        return !mChefUser.getmPhoneNumber().equals(phoneNumber) || !mChefUser.getmHomeAddress().equals(homeAddress);
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