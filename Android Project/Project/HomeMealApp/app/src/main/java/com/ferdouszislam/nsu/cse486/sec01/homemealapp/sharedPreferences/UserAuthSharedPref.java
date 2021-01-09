package com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences;

import android.content.Context;

/**
 * Shared preference handling class for
 * required user authentication information
 */
public class UserAuthSharedPref extends SharedPrefsUtil{

    public static UserAuthSharedPref build(Context context){

        return new UserAuthSharedPref(SharedPrefKeysUtil.USER_AUTHENTICATION_SHARED_PREF_ID, context);
    }

    private UserAuthSharedPref(String mSharedPreferenceId, Context mContext) {
        super(mSharedPreferenceId, mContext);
    }

    public void setUserType(String userType){

        saveStringData(SharedPrefKeysUtil.USER_TYPE_KEY, userType);
    }

    public String getUserType(){

        return loadStringData(SharedPrefKeysUtil.USER_TYPE_KEY);
    }
}
