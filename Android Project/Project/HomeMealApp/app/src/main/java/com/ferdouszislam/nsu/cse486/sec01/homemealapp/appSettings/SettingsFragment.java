package com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;

public class SettingsFragment extends PreferenceFragmentCompat {

    // notification enable/disable swith
    private SwitchPreference mNotificationSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings_preferences, rootKey);

        mNotificationSwitch = findPreference(getString(R.string.notification_switch_preference_key));

        if(mNotificationSwitch!=null) {
            mNotificationSwitch.setOnPreferenceChangeListener((preference, newValue) -> {

                if ((Boolean) newValue) {

                    startNotificationService();

                } else {

                    stopNotificationService();
                }

                return true;
            });
        }

    }

    private void startNotificationService() {

        if(getContext()!=null) {
            getContext().startService(new Intent(getContext(), NotificationService.class));
        }
    }

    private void stopNotificationService() {

        if(getContext()!=null) {
            getContext().stopService(new Intent(getContext(), NotificationService.class));
        }
    }
}