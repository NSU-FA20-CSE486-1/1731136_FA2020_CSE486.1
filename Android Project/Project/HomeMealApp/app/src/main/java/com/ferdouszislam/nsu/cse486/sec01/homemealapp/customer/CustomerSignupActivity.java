package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.EmailPasswordAuthUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.CustomerUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.CustomerUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CustomerUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.CustomerUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class CustomerSignupActivity extends AppCompatActivity {

    private static final String TAG = "CusSA-debug";

    // ui
    private EditText mEmailEditText, mPhoneNumberEditText;
    private EditText mPasswordEditText, mConfirmPasswordEditText;
    private Button mSignupButton;

    // model
    private CustomerUser mCustomerUser;

    // sign up authentication variables
    private Authentication mAuth;
    private EmailPasswordAuthUser mEmailPasswordAuthUser;
    private Authentication.RegisterUserAuthenticationCallbacks mRegistrationAuthCallbacks =
            new Authentication.RegisterUserAuthenticationCallbacks() {
                @Override
                public void onRegistrationSuccess(AuthenticationUser user) {

                    mCustomerUser.setmUid(user.getmUid());

                    storeUserInformationInDatabase();
                }

                @Override
                public void onRegistrationFailure(String message) {

                    progressCompleteUI();

                    showToast(getString(R.string.registration_error));

                    Log.d(TAG, "onRegistrationFailure: signup registration failed -> "+message);
                }
            };

    // variables to store user sign up info to database
    private CustomerUserDao mCustomerUserDao;


    // shared preference to store user type
    private UserAuthSharedPref mUserAuthSharedPref;


    // shared preference to store customer user profile
    private CustomerUserProfileSharedPref mCustomerUserProfileSharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);

        init();
    }

    private void init() {

        setupToolbar();

        mEmailEditText = findViewById(R.id.customerSignupEmail_EditText);
        mPhoneNumberEditText = findViewById(R.id.customerSignupPhone_editText);
        mPasswordEditText = findViewById(R.id.customerSignupPassword_editText);
        mConfirmPasswordEditText = findViewById(R.id.customerSignupConfirmPassword_editText);
        mSignupButton = findViewById(R.id.customerSignup_Button);

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
            // don't show default(app_name) title
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    /*
    "Sign up" button click listener
     */
    public void signupClick(View view) {

        String email = mEmailEditText.getText().toString().trim();
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if(validateInputs(email, phoneNumber, password, confirmPassword)){

            mCustomerUser = new CustomerUser();
            mCustomerUser.setmEmail(email);
            mCustomerUser.setmPhoneNumber(phoneNumber);

            mEmailPasswordAuthUser = new EmailPasswordAuthUser();
            mEmailPasswordAuthUser.setmEmail(email);
            mEmailPasswordAuthUser.setmPassword(password);

            inProgressUI();

            mAuth = new FirebaseEmailPasswordAuthentication(mRegistrationAuthCallbacks, mEmailPasswordAuthUser);

            registerUser(mAuth);
        }

    }

    /**
     * start the user registration authentication process
     * @param auth authenticator object
     */
    private void registerUser(Authentication auth) {

        auth.registerUserAuthentication();
    }

    private boolean validateInputs(String email, String phoneNumber, String password, String confirmPassword) {

        boolean isValid = true;

        if(!InputValidatorUtil.isValidEmail(email)){

            mEmailEditText.setError(getString(R.string.email_input_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isValidPhoneNumber(phoneNumber)){

            mPhoneNumberEditText.setError(getString(R.string.phone_input_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isValidPassword(password)){

            mPasswordEditText.setError(getString(R.string.password_input_error));
            isValid = false;
        }
        if(!confirmPassword.equals(password)){

            mConfirmPasswordEditText.setError(getString(R.string.confirm_password_error));
            isValid = false;
        }

        return isValid;
    }

    /*
    upload user information taken in signup form to the database
     */
    private void storeUserInformationInDatabase() {

        mCustomerUserDao = new CustomerUserFirebaseRealtimeDao();
        mCustomerUserDao.createWithId(mCustomerUser, new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

                storeUserInSharedPrefs();

                openCustomerHomeActivity();
            }

            @Override
            public void onFailed(String failedResponse) {

                progressCompleteUI();
                Log.d(TAG, "onFailed: store signup info error -> "+failedResponse);
            }
        });
    }

    /*
    save usertype as "customer_user" in shared pref
     */
    private void storeUserInSharedPrefs() {

        mUserAuthSharedPref = UserAuthSharedPref.build(this);
        mUserAuthSharedPref.setUserType(UserType.CUSTOMER);

        mCustomerUserProfileSharedPref = CustomerUserProfileSharedPref.build(this);
        mCustomerUserProfileSharedPref.setCustomerUser(mCustomerUser);
    }

    private void openCustomerHomeActivity() {

        Intent intent = new Intent(this, CustomerHomeActivity.class);
        // clear out all activities on the back stack
        // so that back press from this point on closes the app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    /*
    UI to show user while registration is in progress
     */
    private void inProgressUI() {

        mSignupButton.setText(getString(R.string.signup_button_label_signing_up));
        mSignupButton.setEnabled(false);
    }

    /*
    authentication/database upload progress finished UI event
     */
    private void progressCompleteUI() {

        mSignupButton.setText(getString(R.string.signup_ButtonLabel));
        mSignupButton.setEnabled(true);
    }

    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }
}