package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.SystemRebootTimeSettings;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.salarium.bundy.R;

public class SystemRebootTimePreference extends Preference implements TimePickerDialog.OnTimeSetListener {
    private Activity activity;
    private TimePickerDialog timePickerDialog;

    public SystemRebootTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.activity = (Activity) context;
        timePickerDialog = new TimePickerDialog(context, this, 0, 0, false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SystemRebootTimeSettings systemRebootTimeSettings =
            new SystemRebootTimeSettings(
                new SimplePersistence(activity), activity);

        systemRebootTimeSettings.setRebootTimeMinute(minute);
        systemRebootTimeSettings.setRebootTimeHour(hourOfDay);


    }

    @Override
    public void onClick() {
        timePickerDialog.show();
    }
}
