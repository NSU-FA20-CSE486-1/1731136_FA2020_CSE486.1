package com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences;

import android.content.Context;
import android.util.Log;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.models.CustomerUser;
import com.google.gson.Gson;

public class CustomerUserSharedPref extends SharedPrefsUtil {

    private static final String TAG = "CusUSP-debug";

    private Gson gson = new Gson();

    public static CustomerUserSharedPref build(Context context){

        return new CustomerUserSharedPref(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_PREF_ID, context);
    }

    private CustomerUserSharedPref(String mSharedPreferenceId, Context mContext) {
        super(mSharedPreferenceId, mContext);
    }

    public CustomerUser getChefUser(){

        String customerUserJson = loadStringData(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_KEY);

        Log.d(TAG, "setChefUser: saving chef user -> "+customerUserJson);

        return gson.fromJson(customerUserJson, CustomerUser.class);
    }

    public void setChefUser(CustomerUser customerUser){

        String customerUserJson = gson.toJson(customerUser);

        Log.d(TAG, "setChefUser: saving chef user -> "+customerUserJson);

        saveStringData(SharedPrefKeysUtil.CUSTOMER_USER_PROFILE_KEY, customerUserJson);
    }
}
