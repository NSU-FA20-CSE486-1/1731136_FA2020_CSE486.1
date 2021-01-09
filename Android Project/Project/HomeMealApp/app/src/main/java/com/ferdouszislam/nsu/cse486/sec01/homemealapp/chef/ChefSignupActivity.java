package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

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
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class ChefSignupActivity extends AppCompatActivity {

    private static final String TAG = "CSA-debug";

    // ui
    private EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private EditText mPhoneNumberEditText, mHomeAddressEditText;
    private Button mSignupButton;

    // model
    private ChefUser mChefUser;

    // sign up authentication variables
    private Authentication mAuth;
    private EmailPasswordAuthUser mEmailPasswordAuthUser;
    private Authentication.RegisterUserAuthenticationCallbacks mRegistrationAuthCallbacks =
            new Authentication.RegisterUserAuthenticationCallbacks() {
                @Override
                public void onRegistrationSuccess(AuthenticationUser user) {

                    mChefUser.setmUid(user.getmUid());

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
    private ChefUserDao mChefUserDao;


    // shared preference to store user type
    private UserAuthSharedPref mUserAuthSharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_signup);

        init();
    }

    private void init() {

        setupToolbar();

        mPhoneNumberEditText = findViewById(R.id.chefSignup_PhoneNumber_EditText);
        mHomeAddressEditText = findViewById(R.id.chefSignup_address_EditText);
        mEmailEditText = findViewById(R.id.chefSignup_email_EditText);
        mPasswordEditText = findViewById(R.id.chefSignup_password_EditText);
        mConfirmPasswordEditText = findViewById(R.id.chefSignup_confirmPassword_EditText);
        mSignupButton = findViewById(R.id.chefSignup_Button);
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
            // don't show default(app_name) title
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    public void signupClick(View view) {

        String email = mEmailEditText.getText().toString().trim();
        String homeAddress = mHomeAddressEditText.getText().toString().trim();
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if(validateInputs(email, phoneNumber, homeAddress, password, confirmPassword)){

            mChefUser = new ChefUser();
            mChefUser.setmEmail(email);
            mChefUser.setmHomeAddress(homeAddress);
            mChefUser.setmPhoneNumber(phoneNumber);

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

    private boolean validateInputs(String email, String phoneNumber, String homeAddress, String password, String confirmPassword) {

        boolean isValid = true;

        if(!InputValidatorUtil.isValidEmail(email)){

            mEmailEditText.setError(getString(R.string.email_input_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isValidPhoneNumber(phoneNumber)){

            mPhoneNumberEditText.setError(getString(R.string.phone_input_error));
            isValid = false;
        }
        if(!InputValidatorUtil.isValidHomeAddress(homeAddress)){

            mHomeAddressEditText.setError(getString(R.string.address_input_error));
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

        mChefUserDao = new ChefUserFirebaseRealtimeDao();
        mChefUserDao.createWithId(mChefUser, new DatabaseOperationStatusListener<Void, String>() {
            @Override
            public void onSuccess(Void successResponse) {

                storeUserTypeInSharedPrefs();

                openChefHomeActivity();
            }

            @Override
            public void onFailed(String failedResponse) {

                progressCompleteUI();
                Log.d(TAG, "onFailed: store signup info error -> "+failedResponse);
            }
        });
    }

    /*
    save usertype as "chef_user" in shared pref
     */
    private void storeUserTypeInSharedPrefs() {

        mUserAuthSharedPref = UserAuthSharedPref.build(this);
        mUserAuthSharedPref.setUserType(UserType.CHEF);
    }

    private void openChefHomeActivity() {

        Intent intent = new Intent(this, ChefHomeActivity.class);
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