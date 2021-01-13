package com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences;

import android.content.Context;
import android.util.Log;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.google.gson.Gson;

/**
 * Shared preference handling class for
 * required chef user profile information
 */
public class ChefUserProfileSharedPref extends SharedPrefsUtil {

    private static final String TAG = "CUPSP-debug";

    private Gson gson = new Gson();

    public static ChefUserProfileSharedPref build(Context context){

        return new ChefUserProfileSharedPref(SharedPrefKeysUtil.CHEF_USER_LOCATION_SHARED_PREF_ID, context);
    }

    private ChefUserProfileSharedPref(String mSharedPreferenceId, Context mContext) {
        super(mSharedPreferenceId, mContext);
    }

    public ChefUser getChefUser(){

        String chefUserJson = loadStringData(SharedPrefKeysUtil.CHEF_USER_PROFILE_KEY);

        Log.d(TAG, "setChefUser: saving chef user -> "+chefUserJson);

        return gson.fromJson(chefUserJson, ChefUser.class);
    }

    public void setChefUser(ChefUser chefUser){

        String chefUserJson = gson.toJson(chefUser);

        Log.d(TAG, "setChefUser: saving chef user -> "+chefUserJson);

        saveStringData(SharedPrefKeysUtil.CHEF_USER_PROFILE_KEY, chefUserJson);
    }

}
