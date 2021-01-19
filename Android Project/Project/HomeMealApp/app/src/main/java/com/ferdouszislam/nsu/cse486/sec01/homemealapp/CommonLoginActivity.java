package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings.SettingsFragment;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.EmailPasswordAuthUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.ChefSignupActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.CustomerHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.CustomerSignupActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.CustomerUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.CustomerUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CustomerUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.ChefUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.CustomerUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
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

            mEmailPasswordAuthUser.setmUid(user.getmUid());

            storeUserTypeToSharedPref(mUserType);

            loadUserProfileBasedHomeActivity(mUserType, user.getmUid());
        }

        @Override
        public void onAuthenticationFailure(String message) {

            loginAuthenticationFailedUI();
            progressCompleteUI();
        }
    };

    // variable to read chef user profile from database
    private ChefUserDao mChefUserDao;
    private SingleDataChangeListener<ChefUser> mChefSingleDataChangeListener = new SingleDataChangeListener<ChefUser>() {
        @Override
        public void onDataChange(ChefUser data) {

            ChefUserProfileSharedPref.build(CommonLoginActivity.this).setChefUser(data);

            openActivityBasedHomeActivity(mUserType);
        }
    };

    // variable to read customer user profile from database
    private CustomerUserDao mCustomerUserDao;
    private SingleDataChangeListener<CustomerUser> mCustomerSingleDataChangeListener = new SingleDataChangeListener<CustomerUser>() {
        @Override
        public void onDataChange(CustomerUser data) {

            CustomerUserProfileSharedPref.build(CommonLoginActivity.this).setCustomerUser(data);

            openActivityBasedHomeActivity(mUserType);
        }
    };


    /**
     * save user type to shared preferences
     * @param userType selected user type
     */
    private void storeUserTypeToSharedPref(String userType) {

        // TODO: remove added log for tracing notification service bug
        Log.d(TAG, "storeUserTypeToSharedPref: saving user type to sp -> "+userType);

        mUserAuthSharedPref = UserAuthSharedPref.build(CommonLoginActivity.this);
        mUserAuthSharedPref.setUserType(userType);

        // TODO: remove added log for tracing notification service bug
        Log.d(TAG, "storeUserTypeToSharedPref: user type saved = "+mUserAuthSharedPref.getUserType());
    }

    /**
     * load user profile based on user type
     * @param userType selected user type
     */
    private void loadUserProfileBasedHomeActivity(String userType, String uid) {

        DatabaseOperationStatusListener<Void, String> statusListener =
                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {
                        // do nothing
                    }

                    @Override
                    public void onFailed(String failedResponse) {

                        // MUST CALL SIGN OUT HERE BECAUSE AUTHENTICATION PASSED BUT USER TYPE DID NOT
                        // THIS IS OCCURRING BECAUSE BOTH USERS USE THE SAME AUTHENTICATION SYSTEM
                        mAuth.signOut();

                        progressCompleteUI();
                        loginAuthenticationFailedUI();
                    }
                };

        if(userType.equals(UserType.CHEF)){

            mChefUserDao.readWithId(uid, mChefSingleDataChangeListener, statusListener);
        }

        else if(userType.equals(UserType.CUSTOMER)){

            mCustomerUserDao.readWithId(uid, mCustomerSingleDataChangeListener, statusListener);

        }
        //else if(userType.equals(UserType.DELIVERY_GUY)) ;
    }

    /**
     * open home activity based on user type
     * @param userType selected user type
     */
    private void openActivityBasedHomeActivity(String userType) {

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

        mChefUserDao = new ChefUserFirebaseRealtimeDao();

        mCustomerUserDao = new CustomerUserFirebaseRealtimeDao();
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

            inProgressUI();

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

        return InputValidatorUtil.isValidEmail(email) && InputValidatorUtil.isValidPassword(password);
    }

    public void dontHaveAnAccountClicked(View view) {

        if(mUserType.equals(UserType.CUSTOMER)){

            startActivity(new Intent(this, CustomerSignupActivity.class));
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