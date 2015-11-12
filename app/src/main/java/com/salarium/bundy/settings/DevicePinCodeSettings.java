package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import android.app.Activity;

public class DevicePinCodeSettings extends Settings {
    private final Activity activity;
    private final SimplePersistence simplePersistence;
    private final int ID;
    private final String defaultPinCode = "1234";

    public DevicePinCodeSettings
        (SimplePersistence simplePersistence, Activity activity) {

        this.simplePersistence = simplePersistence;
        this.activity = activity;
        this.ID = R.string.pref_device_pin_id;
    }

    @Override
    public boolean complete() {
        return !getDevicePinCode().isEmpty();
    }

    public String getDevicePinCode() {
        String devicePin = simplePersistence
            .getPersistence(activity.getString(ID));
        if (devicePin.isEmpty()) {
            return defaultPinCode;
        }
        return devicePin;
    }
}
