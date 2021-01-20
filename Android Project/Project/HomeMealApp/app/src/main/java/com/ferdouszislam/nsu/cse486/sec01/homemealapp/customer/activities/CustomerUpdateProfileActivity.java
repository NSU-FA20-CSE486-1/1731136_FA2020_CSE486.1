package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.activities;

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
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.CustomerUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.CustomerUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CustomerUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.CustomerUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class CustomerUpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "CustUPA-debug";

    // ui
    private EditText mPhoneNumberEditText;
    private Button mUpdateButton;

    // model
    private CustomerUser mCustomerUser;

    // shared preference variable for updating local chef user profile
    private CustomerUserProfileSharedPref mCustomerUserProfileSharedPref;

    // variable to read and update in database
    private CustomerUserDao mCustomerUserDao;
    private SingleDataChangeListener<CustomerUser> mSingleDataChangeListener = new SingleDataChangeListener<CustomerUser>() {
        @Override
        public void onDataChange(CustomerUser data) {

            mCustomerUser = data;

            mCustomerUserProfileSharedPref.setCustomerUser(mCustomerUser);

            populateInputFields(mCustomerUser);
        }
    };

    // variables for user authentication
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            mCustomerUserDao = new CustomerUserFirebaseRealtimeDao();
            loadCustomerUserDataFromDatabase(user.getmUid(), mCustomerUserDao, mSingleDataChangeListener);
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(CustomerUpdateProfileActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(CustomerUpdateProfileActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };


    /**
     * read user existing information from database
     * @param id user id to search
     * @param customerUserDao dao object
     */
    private void loadCustomerUserDataFromDatabase(String id, CustomerUserDao customerUserDao,
                                                  SingleDataChangeListener<CustomerUser> singleDataChangeListener) {

        customerUserDao.readWithId(
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

        // use the same layout as "Customer Sign-up"
        setContentView(R.layout.activity_customer_signup);

        init();
    }

    private void init() {

        setupToolbar();

        mPhoneNumberEditText = findViewById(R.id.customerSignupPhone_editText);
        mUpdateButton = findViewById(R.id.customerSignup_Button);
        mUpdateButton.setText(getString(R.string.save));
        mUpdateButton.setOnClickListener(v -> updateButtonClick());

        hideUnnecessaryInputFields();

        inProgressUI();

        mAuth = new FirebaseEmailPasswordAuthentication();
        authenticateUser(mAuth, mAuthCallbacks);

        mCustomerUserProfileSharedPref = CustomerUserProfileSharedPref.build(this);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerSignup_Toolbar);
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


    private void hideUnnecessaryInputFields() {

        findViewById(R.id.customerSignupWelcome_TextView).setVisibility(View.GONE);
        findViewById(R.id.customerSignupEmail_EditText).setVisibility(View.GONE);
        findViewById(R.id.customerSignUpEmailHint_TextView).setVisibility(View.GONE);
        findViewById(R.id.customerSignupPassword_editText).setVisibility(View.GONE);
        findViewById(R.id.customerSignUpPasswordHint_TextView).setVisibility(View.GONE);
        findViewById(R.id.customerSignupConfirmPassword_editText).setVisibility(View.GONE);
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
     * populate the input fields with data of the
     * customer user's existing profile information
     * @param customerUser customer user model
     */
    private void populateInputFields(CustomerUser customerUser) {

        mPhoneNumberEditText.setText(customerUser.getmPhoneNumber());
    }


    /*
    "save" button click listener
     */
    private void updateButtonClick() {

        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();

        if(validateInputs(phoneNumber) && dataWasUpdated(phoneNumber)){

            mCustomerUser.setmPhoneNumber(phoneNumber);

            inProgressUI();

            updateCustomerUserProfile(mCustomerUser, mCustomerUserDao, mCustomerUserProfileSharedPref);
        }
    }

    /**
     * update chef user's profile data
     * @param customerUser updated chef user object
     * @param customerUserDao dao object
     * @param customerUserProfileSharedPref shared pref object for chef user profile info
     */
    private void updateCustomerUserProfile(CustomerUser customerUser, CustomerUserDao customerUserDao,
                                       CustomerUserProfileSharedPref customerUserProfileSharedPref) {

        customerUserDao.updateWithId(customerUser, customerUser.getmUid(), new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

                customerUserProfileSharedPref.setCustomerUser(customerUser);

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
     * @param phoneNumber updated phone numbe
     * @return if inputs are valid
     */
    private boolean validateInputs(String phoneNumber) {

        boolean isValid = true;

        if(!InputValidatorUtil.isValidPhoneNumber(phoneNumber)){

            mPhoneNumberEditText.setError(getString(R.string.phone_input_error));
            isValid = false;
        }

        return isValid;
    }

    /**
     * check whether data was updated or not
     * @param phoneNumber phone number input
     * @return if data was changed or not
     */
    private boolean dataWasUpdated(String phoneNumber) {

        return !mCustomerUser.getmPhoneNumber().equals(phoneNumber);
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