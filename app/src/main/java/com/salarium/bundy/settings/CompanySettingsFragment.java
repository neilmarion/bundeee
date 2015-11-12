package com.salarium.bundy.settings;

import com.salarium.bundy.R;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class CompanySettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.company_settings);
    }
}
