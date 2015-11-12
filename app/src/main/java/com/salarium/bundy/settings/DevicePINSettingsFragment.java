package com.salarium.bundy.settings;

import com.salarium.bundy.R;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class DevicePINSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.device_pin_settings);
    }
}
