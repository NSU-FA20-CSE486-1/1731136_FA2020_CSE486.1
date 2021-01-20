package com.ferdouszislam.nsu.cse486.sec01.homemealapp.commonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.activities.ChefHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.activities.CustomerHomeActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

/**
 * Activity for welcome page
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MA-debug";

    // user authentication variables (to check if user is logged in or not)
    private Authentication mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        mAuth = new FirebaseEmailPasswordAuthentication(new Authentication.AuthenticationCallbacks() {
            @Override
            public void onAuthenticationSuccess(AuthenticationUser user) {

                String userType = UserAuthSharedPref.build(MainActivity.this).getUserType();

                if(userType==null) mAuth.signOut();

                else{

                    if(userType.equals(UserType.CHEF)) startActivity(new Intent(MainActivity.this, ChefHomeActivity.class));

                    else if(userType.equals(UserType.CUSTOMER)) startActivity(new Intent(MainActivity.this, CustomerHomeActivity.class));

                    //else if(userType.equals(UserType.DELIVERY_GUY)) return;

                    else return;

                    finish();
                }
            }

            @Override
            public void onAuthenticationFailure(String message) {

                Log.d(TAG, "onAuthenticationFailure: user not logged in");
            }
        });

        mAuth.authenticateUser();
    }

    /*
    "GET STARTED" button click listener
     */
    public void getStartedClick(View view) {

        startActivity(new Intent(this, UserTypeChoiceActivity.class));

        // stop opening this activity on back press
        finish();
    }
}