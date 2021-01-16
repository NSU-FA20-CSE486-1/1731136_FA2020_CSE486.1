package com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String NOTIFICATION_SERVICE_UID_KEY = "common-notification-service-uid-key";

    // parent Activity
    private SettingsActivity mParentActivity;

    // notification enable/disable swith
    private SwitchPreference mNotificationSwitch;

    // user authentication id
    private String mUid;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(String mUid, SettingsActivity mParentActivity) {

        this.mParentActivity = mParentActivity;
        this.mUid = mUid;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings_preferences, rootKey);

        mNotificationSwitch = findPreference(getString(R.string.notification_switch_preference_key));

        if(mNotificationSwitch!=null) {
            mNotificationSwitch.setOnPreferenceChangeListener((preference, newValue) -> {

                if ((Boolean) newValue) {

                    // doesn't work if we start here
                    //startNotificationService(mParentActivity);

                } else {

                    // doesn't work if we start here
                    //stopNotificationService(mParentActivity);
                }

                return true;
            });
        }

    }

    private void startNotificationService(SettingsActivity settingsActivity) {

        if(settingsActivity==null) return;

        Intent intent = new Intent(settingsActivity, NotificationService.class);

        intent.putExtra(NOTIFICATION_SERVICE_UID_KEY, mUid);

        settingsActivity.startService(intent);
    }

    private void stopNotificationService(SettingsActivity settingsActivity) {

        if(settingsActivity!=null) {
            settingsActivity.stopService(new Intent(settingsActivity, NotificationService.class));
        }
    }
}