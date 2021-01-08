package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.EmailPasswordAuthUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidator;

public class ChefSignupActivity extends AppCompatActivity {

    // ui
    private EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private EditText mPhoneNumberEditText, mHomeAddressEditText;
    private Button mSignupButton;

    // model
    private ChefUser mChefUser;

    // signup authentication variables
    private Authentication mAuth;
    private EmailPasswordAuthUser mEmailPasswordAuthUser;
    private Authentication.RegisterUserAuthenticationCallbacks mRegistrationAuthCallbacks =
            new Authentication.RegisterUserAuthenticationCallbacks() {
                @Override
                public void onRegistrationSuccess(AuthenticationUser user) {

                    storeUserInformationInDatabase();
                }

                @Override
                public void onRegistrationFailure(String message) {

                    progressCompleteUI();

                    showToast(getString(R.string.registration_error));
                }
            };

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

        mChefUser = new ChefUser();
        mAuth = new FirebaseEmailPasswordAuthentication();
        mEmailPasswordAuthUser = new EmailPasswordAuthUser();
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

            // TODO: remove toast
            showToast("inputs valid!");

            mChefUser.setmEmail(email);
            mChefUser.setmHomeAddress(homeAddress);
            mChefUser.setmPhoneNumber(phoneNumber);

            mEmailPasswordAuthUser.setmEmail(email);
            mEmailPasswordAuthUser.setmPassword(password);
            registerUser();
        }
    }

    /*
    start the user registration authentication process
     */
    private void registerUser() {

        // TODO: implement
    }

    private boolean validateInputs(String email, String phoneNumber, String homeAddress, String password, String confirmPassword) {

        boolean isValid = true;

        if(!InputValidator.isValidEmail(email)){

            mEmailEditText.setError(getString(R.string.email_input_error));
            isValid = false;
        }
        if(!InputValidator.isValidPhoneNumber(phoneNumber)){

            mPhoneNumberEditText.setError(getString(R.string.phone_input_error));
            isValid = false;
        }
        if(!InputValidator.isValidHomeAddress(homeAddress)){

            mHomeAddressEditText.setError(getString(R.string.address_input_error));
            isValid = false;
        }
        if(!InputValidator.isValidPassword(password)){

            mPasswordEditText.setError(getString(R.string.password_input_error));
            isValid = false;
        }
        if(!confirmPassword.equals(password)){

            mConfirmPasswordEditText.setError(getString(R.string.confirm_password_error));
            isValid = false;
        }

        return isValid;
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
    }

    /*
    upload user information taken in signup form to the database
     */
    private void storeUserInformationInDatabase() {

        // TODO: implement
    }

    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }
}