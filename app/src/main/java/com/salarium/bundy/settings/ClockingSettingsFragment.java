package com.salarium.bundy.settings;

import com.salarium.bundy.R;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class ClockingSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.clocking_settings);
    }
}
