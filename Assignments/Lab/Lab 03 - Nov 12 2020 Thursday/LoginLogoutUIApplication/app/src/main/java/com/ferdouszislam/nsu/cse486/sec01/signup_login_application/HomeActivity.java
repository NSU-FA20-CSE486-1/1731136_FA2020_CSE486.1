package com.ferdouszislam.nsu.cse486.sec01.signup_login_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    // model
    private User loggedInUser;

    // ui
    private TextView welcomeUserTextView;
    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }

    private void init() {
    // initialize UI elements and model

        welcomeUserTextView = findViewById(R.id.toolbar_welcome_textview);
        logout_button = findViewById(R.id.logout_button);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // logout by terminating this activity and start login activity

                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });

        grabAndShowDataFromMainActivity();

    }

    private void grabAndShowDataFromMainActivity() {
    // load data passed from main activity to initialize model and show in UI

        Bundle bundle = getIntent().getExtras();

        String username = bundle.getString(MainActivity.USERNAME_KEY);
        String password = bundle.getString(MainActivity.PASSWORD_KEY);

        // initialize model
        loggedInUser = new User(username, password);

        // show loaded data in UI
        welcomeUserTextView.setText( getString(R.string.app_toolbar_text) +" "+ loggedInUser.getUsername());

    }


}