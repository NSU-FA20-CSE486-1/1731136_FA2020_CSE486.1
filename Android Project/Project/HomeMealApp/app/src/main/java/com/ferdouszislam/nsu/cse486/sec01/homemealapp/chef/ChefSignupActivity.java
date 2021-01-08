package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.EmailPasswordAuthUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;

public class ChefSignupActivity extends AppCompatActivity {

    // ui
    private EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private EditText mPhoneNumberEditText, mHomeAddressEditText;

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

        Intent intent = new Intent(this, ChefHomeActivity.class);

        // clear out all activities on the back stack
        // so that back press from this point on closes the app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
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
    }

    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }
}