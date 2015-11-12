package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;

import android.app.Activity;

public class SettingsManager {
    Activity activity;
    AdminAccountSettings adminAccountSettings;
    ClockingMethodSettings clockingMethodSettings;
    DevicePinCodeSettings devicePinCodeSettings;
    SimplePersistence simplePersistence;

    public SettingsManager(Activity activity) {
        this.activity = activity;
        this.simplePersistence = new SimplePersistence(activity);
        this.adminAccountSettings =
            new AdminAccountSettings(simplePersistence, activity);
        this.clockingMethodSettings =
            new ClockingMethodSettings(simplePersistence, activity);
        this.devicePinCodeSettings =
            new DevicePinCodeSettings(simplePersistence, activity);
    }

    public boolean isComplete() {
        /*
            TODO:
            Things to check for:
            1. Admin PIN for the device
            2. Connected Salarium admin account
            3. At least one (1) identification method
        */

        return adminAccountSettings.complete() &&
                clockingMethodSettings.complete() &&
                devicePinCodeSettings.complete();
    }

    public ClockingMethodSettings getClockingMethodSettings() {
        return clockingMethodSettings;
    }

    public AdminAccountSettings getAdminAccountSettings() {
        return adminAccountSettings;
    }

    public DevicePinCodeSettings getDevicePinCodeSettings() {
        return devicePinCodeSettings;
    }
}
