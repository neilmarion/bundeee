package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

public class EmployeeIDClockingMethodPreference extends SwitchPreference {
    SwitchPreferenceChangePositiveListener switchPreferenceChangePositiveListener;

    public EmployeeIDClockingMethodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceChangeListener(context);
    }

    protected void setPreferenceChangeListener(
        Context context
    ) {
        final Context c = context;
        final SwitchPreferenceChangePositiveListener s =
            switchPreferenceChangePositiveListener;

        setOnPreferenceChangeListener(
            new Preference.OnPreferenceChangeListener() {
                private int count = 0;

                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue) {

                    ClockingMethodSettings clockingMethodSettings =
                        new ClockingMethodSettings(
                            new SimplePersistence(c), (Activity) c);
                    if (count != 0) {
                        if (!((Boolean) newValue)) {
                            if(clockingMethodSettings.
                                isIdentificationMethod(preference.getKey())) {

                                if (clockingMethodSettings.
                                    isOnlyOneIdentificationSet()) {

                                    ViewManager.
                                        showAlertDialog(
                                            c,
                                            Values.Messages.Error.
                                                ONLY_ONE_IDENTIFICATION_LEFT);
                                    return false;
                                }
                            }
                        }
                    }

                    count++;
                    return true;
                }
        });
    }
}
