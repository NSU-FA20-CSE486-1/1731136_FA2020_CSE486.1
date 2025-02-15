package com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences;

import android.content.Context;
import android.util.Log;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CustomerUser;
import com.google.gson.Gson;

public class CustomerUserProfileSharedPref extends SharedPrefsUtil {

    private static final String TAG = "CusUSP-debug";

    private Gson gson = new Gson();

    public static CustomerUserProfileSharedPref build(Context context){

        return new CustomerUserProfileSharedPref(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_PREF_ID, context);
    }

    private CustomerUserProfileSharedPref(String mSharedPreferenceId, Context mContext) {
        super(mSharedPreferenceId, mContext);
    }

    public CustomerUser getCustomerUser(){

        String customerUserJson = loadStringData(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_KEY);

        Log.d(TAG, "setChefUser: saving chef user -> "+customerUserJson);

        return gson.fromJson(customerUserJson, CustomerUser.class);
    }

    public void setCustomerUser(CustomerUser customerUser){

        String customerUserJson = gson.toJson(customerUser);

        Log.d(TAG, "setChefUser: saving chef user -> "+customerUserJson);

        saveStringData(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_KEY, customerUserJson);
    }
}
