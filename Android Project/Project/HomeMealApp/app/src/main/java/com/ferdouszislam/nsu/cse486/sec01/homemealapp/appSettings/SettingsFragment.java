package com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;

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
                    // TODO: start notification service
                    Toast.makeText(getContext(), "turn on notifications!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // TODO: stop notification service
                }

                return true;
            });
        }

    }
}