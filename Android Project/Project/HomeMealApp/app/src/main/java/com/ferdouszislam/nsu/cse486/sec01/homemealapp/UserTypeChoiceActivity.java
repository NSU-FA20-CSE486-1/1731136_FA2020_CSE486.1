package com.ferdouszislam.nsu.cse486.sec01.homemealapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class UserTypeChoiceActivity extends AppCompatActivity {

    // key for passing data to next activity
    public static final String SELECTED_USER_TYPE_KEY = "selected-user-type-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_choice);
    }

    public void customerUserClick(View view) {

        startNextActivity(UserType.CUSTOMER);
    }

    public void chefUserClick(View view) {

        startNextActivity(UserType.CHEF);
    }

    public void deliveryUserClick(View view) {

        startNextActivity(UserType.DELIVERY_GUY);
    }


    /*
    start next activity based on user type selection
     */
    private void startNextActivity(String userType) {

        Intent intent = new Intent(this, CommonLoginActivity.class);
        intent.putExtra(SELECTED_USER_TYPE_KEY, userType);
        startActivity(intent);
    }
}