package com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * abstract class to be extended by classes for
 * each individual SharedPreference
 */
public abstract class SharedPrefsUtil {

    private String mSharedPreferenceId;
    private Context mContext;

    public SharedPrefsUtil(String mSharedPreferenceId, Context mContext) {
        this.mSharedPreferenceId = mSharedPreferenceId;
        this.mContext = mContext;
    }

    /**
     * save a string to shared preferences
     * @param key shared pref key
     * @param data string to be save
     */
    protected void saveStringData(String key, String data){

        getSharedPreference().edit().putString(key, data).apply();
    }

    /**
     * load shared pref string data
     * @param key shared pref key
     * @return data stored by shared pref
     */
    protected String loadStringData(String key){

        return getSharedPreference().getString(key, null);
    }

    /*
    Clears out all shared pref data associated with this object's "mSharedPreferenceId"
     */
    public void clearAllSharedPreferences(){

        getSharedPreference().edit().clear().apply();
    }

    private SharedPreferences getSharedPreference(){

        return mContext.getSharedPreferences(mSharedPreferenceId, Context.MODE_PRIVATE);
    }

    public String getmSharedPreferenceId() {
        return mSharedPreferenceId;
    }

    public void setmSharedPreferenceId(String mSharedPreferenceId) {
        this.mSharedPreferenceId = mSharedPreferenceId;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
