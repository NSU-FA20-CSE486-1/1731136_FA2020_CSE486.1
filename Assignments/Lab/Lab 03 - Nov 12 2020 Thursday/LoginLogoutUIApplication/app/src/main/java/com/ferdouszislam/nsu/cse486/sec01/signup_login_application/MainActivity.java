package com.ferdouszislam.nsu.cse486.sec01.signup_login_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// controller class

    // model class for login user
    private User user;

    // login success flag
    private boolean loggedIn;
    private final String LOGIN_STATE_KEY = "loginState-key";

    // ui
    private EditText usernameEditText, passwordEditText;

    // intent data passing key
    public static final String USERNAME_KEY = "username-key";
    public static final String PASSWORD_KEY = "password-key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
    // save state variable on screen rotation

        outState.putBoolean(LOGIN_STATE_KEY, loggedIn);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    // retrieve saved state after screen rotation

        loggedIn = savedInstanceState.getBoolean(LOGIN_STATE_KEY);

        if(loggedIn) login();

        super.onRestoreInstanceState(savedInstanceState);
    }



    private void init() {
    // initialize variables

        // ui elements
        usernameEditText = findViewById(R.id.login_EditText);
        passwordEditText = findViewById(R.id.password_EditText);

        // model
        user = new User();

        loggedIn = false;
    }

    public void enterClick(View view) {
    // enter button click

        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        if(user.validate()){
            if(user.authenticate()) {
                login();
            }
            else
                showToast(getString(R.string.authenticationFailed_Toast));
        }

        else
            showToast(getString(R.string.emptyLoginInputField_Toast));

    }

    private void login() {
    // login successful start home page

        loggedIn = true;

        Intent intent = new Intent(this, HomeActivity.class);
        // pass specified data to new activity
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME_KEY, user.getUsername());
        bundle.putString(PASSWORD_KEY, user.getPassword());
        intent.putExtras(bundle);
        // start home activity
        startActivity(intent);

        // terminate login activity
        // to prevent this activity from opening when back pressed
        finish();

    }

    public void sigilClick(View view) {
    // image view click

        usernameEditText.setText(User.ALLOWED_USERNAME);
        passwordEditText.setText(User.ALLOWED_PASSWORD);

        showToast("Valar Morghulis");

    }

    private void showToast(String message){
    // show toast message

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();

    }
}