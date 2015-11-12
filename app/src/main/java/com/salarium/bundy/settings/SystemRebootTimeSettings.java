package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import android.app.Activity;

public class SystemRebootTimeSettings extends Settings {
    private final Activity activity;
    private final SimplePersistence simplePersistence;

    public static final int REBOOT_TIME_KEY_STRING_ID =
        R.string.pref_reboot_time_id;
    public static final String REBOOT_TIME_HOUR_KEY =
        "pref_reboot_time_hour";
    public static final String REBOOT_TIME_MINUTE_KEY =
        "pref_reboot_time_minute";

    public SystemRebootTimeSettings
        (SimplePersistence simplePersistence, Activity activity) {

        this.simplePersistence = simplePersistence;
        this.activity = activity;
    }

    @Override
    public boolean complete() {
        return true;
    }

    public String getRebootTimeString () {
        return String.format("%02d:%02d:00", getRebootTimeHour(), getRebootTimeMinute());
    }

    public int getRebootTimeMinute() {
        return simplePersistence.getIntegerPersistence(REBOOT_TIME_MINUTE_KEY);
    }

    public int getRebootTimeHour() {
        return simplePersistence.getIntegerPersistence(REBOOT_TIME_HOUR_KEY);
    }

    public void setRebootTimeMinute (int minute) {
        simplePersistence.
            persistInteger(REBOOT_TIME_MINUTE_KEY, minute);
    }

    public void setRebootTimeHour (int hour) {
        simplePersistence.
            persistInteger(REBOOT_TIME_HOUR_KEY, hour);
    }
}
