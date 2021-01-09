package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

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

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.EmailPasswordAuthUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefSignupActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.CustomerHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.CustomerSignupActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidator;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class CommonLoginActivity extends AppCompatActivity {

    private static final String TAG = "CLA-debug";

    // ui
    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;

    // selected user type
    private String mUserType;

    // model
    private EmailPasswordAuthUser mEmailPasswordAuthUser;

    // shared preference to store user type
    private UserAuthSharedPref mUserAuthSharedPref;

    // variables for login authentication
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            progressCompleteUI();

            storeUserTypeToSharedPref(mUserType);

            openUserChoiceBasedHomeActivity(mUserType);
        }

        @Override
        public void onAuthenticationFailure(String message) {

            progressCompleteUI();
            loginAuthenticationFailedUI();
        }
    };

    /**
     * save user type to shared preferences
     * @param userType selected user type
     */
    private void storeUserTypeToSharedPref(String userType) {

        mUserAuthSharedPref = UserAuthSharedPref.build(CommonLoginActivity.this);
        mUserAuthSharedPref.setUserType(userType);
    }

    /**
     * open user type based home activity
     * @param userType selected user type
     */
    private void openUserChoiceBasedHomeActivity(String userType) {

        Intent intent;

        if(userType.equals(UserType.CHEF)) intent = new Intent(this, ChefHomeActivity.class);

        else if(userType.equals(UserType.CUSTOMER)) intent = new Intent(this, CustomerHomeActivity.class);

        //else if(userType.equals(UserType.DELIVERY_GUY)) ;

        else return;

        // clear out all activities on the back stack
        // so that back press from this point on closes the app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login);
        
        init();
    }

    private void init() {

        setupToolbar();

        mEmailEditText = findViewById(R.id.loginEmail_EditText);
        mPasswordEditText = findViewById(R.id.loginPassword_EditText);
        mLoginButton = findViewById(R.id.login_Button);

        mUserType = getIntent().getStringExtra(UserTypeChoiceActivity.SELECTED_USER_TYPE_KEY);
        Log.d(TAG, "init: user type = "+mUserType);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.commonLogin_Toolbar);
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
    "Login" button click listener
     */
    public void loginClick(View view) {

        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if(validateInputs(email, password)){

            mEmailPasswordAuthUser = new EmailPasswordAuthUser(email, password);

            mAuth = new FirebaseEmailPasswordAuthentication(mAuthCallbacks, mEmailPasswordAuthUser);

            loginUser(mAuth);
        }

        else loginAuthenticationFailedUI();
    }

    /**
     * log in user
     * @param auth authenticator object
     */
    private void loginUser(Authentication auth) {

        auth.authenticateUser();
    }

    /**
     * validate user input
     * @param email email input
     * @param password password input
     * @return true if valid
     */
    private boolean validateInputs(String email, String password) {

        return InputValidator.isValidEmail(email) && InputValidator.isValidPassword(password);
    }

    public void dontHaveAnAccountClicked(View view) {

        if(mUserType.equals(UserType.CUSTOMER)){

            // TODO: implement
            //startActivity(new Intent(this, CustomerSignupActivity.class));
        }

        else if(mUserType.equals(UserType.CHEF)){

            startActivity(new Intent(this, ChefSignupActivity.class));
        }

        //else if(mUserType.equals(UserType.DELIVERY_GUY))

        else Log.d(TAG, "dontHaveAnAccountClicked: error! no such user type");
    }

    /*
    UI to show user while registration is in progress
     */
    private void inProgressUI() {

        mLoginButton.setText(getString(R.string.login_button_label_logging_in));
        mLoginButton.setEnabled(false);
    }

    /*
    authentication/database upload progress finished UI event
     */
    private void progressCompleteUI() {

        mLoginButton.setText(getString(R.string.login_ButtonLabel));
        mLoginButton.setEnabled(true);
    }

    /*
    UI for when login authentication fails
     */
    private void loginAuthenticationFailedUI(){

        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT)
                .show();
    }
}