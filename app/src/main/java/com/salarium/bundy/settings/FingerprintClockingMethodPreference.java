package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

public class FingerprintClockingMethodPreference
    extends ClockingMethodPreference {

    private Context context;
    private FingerprintDevicesPreference fingerprintDevicesPreference;
    private ClockingMethodSettings clockingMethodSettings;

    public FingerprintClockingMethodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceChangeListener(context);

        this.context = context;
        clockingMethodSettings =
            new ClockingMethodSettings(
                new SimplePersistence(context), (Activity) context);
        fingerprintDevicesPreference =
            new FingerprintDevicesPreference(context, clockingMethodSettings);
    }

    protected void setPreferenceChangeListener(
        Context context
    ) {
        final Context c = context;
        setOnPreferenceChangeListener(
            new Preference.OnPreferenceChangeListener() {
                private int count = 0;

                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue) {

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
                        } else {
                            fingerprintDevicesPreference.showDialog();
                        }
                    }

                    count++;
                    return true;
                }
        });
    }


}
