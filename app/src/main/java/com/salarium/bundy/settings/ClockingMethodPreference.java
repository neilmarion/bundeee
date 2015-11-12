package com.salarium.bundy.settings;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

/** Preference class for setting clocking methods
 * @author Neil Marion dela Cruz
 */
public class ClockingMethodPreference extends SwitchPreference {
    SwitchPreferenceChangePositiveListener switchPreferenceChangePositiveListener;

    public ClockingMethodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
