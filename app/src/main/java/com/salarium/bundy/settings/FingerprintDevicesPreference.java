package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

public class FingerprintDevicesPreference
    extends AlertDialog
    implements DialogInterface.OnClickListener, DeviceListOnSelectListener {
    private Context context;
    private ClockingMethodSettings clockingMethodSettings;
    private String chosenDevice;
    private DeviceList deviceList;
    private final FingerprintDevicesPreference fingerprintDevicesPreferences = this;

    public FingerprintDevicesPreference(
        Context context, ClockingMethodSettings clockingMethodSettings
    ) {

        super(context);
        this.context = context;
        this.clockingMethodSettings = clockingMethodSettings;
        setButton(AlertDialog.BUTTON_POSITIVE,
            Values.Labels.SAVE,
            this);
        setButton(AlertDialog.BUTTON_NEGATIVE,
            Values.Labels.CANCEL,
            this);
        setup(context);
    }

    private void setup(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Context c = context;
        final ClockingMethodSettings cms = clockingMethodSettings;

        LayoutInflater inflater =
            (LayoutInflater) context.
                getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(
            R.layout.fingerprint_devices_preference, null);
        setView(view);
        RadioGroup devices =
            (RadioGroup) view.findViewById(R.id.fingerprint_devices_radio_group);
        devices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.a370:
                        chosenDevice = c.getString(R.string.pref_clocking_settings_verification_fingerprint_device_a370_id);
                        break;
                    case R.id.hf7000:
                        chosenDevice = c.getString(R.string.pref_clocking_settings_verification_fingerprint_device_hf7000_id);
                        deviceList =
                            new DeviceList(c, fingerprintDevicesPreferences);
                        deviceList.showDialog();
                        break;
                }
            }
        });
    }

    public void showDialog() {
        show();
        styleButton();
    }

    private void styleButton() {
        Button negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        negativeButton.
            setBackgroundResource(R.drawable.negative_action_button);
        negativeButton.setPadding(20, 20, 20, 20);

        Button positiveButton = getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setPadding(20, 20, 20, 20);
        positiveButton.setTextColor(Color.parseColor("#ffffff"));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        positiveButton.
            setBackgroundResource(R.drawable.action_button);
        positiveButton.setPadding(20, 20, 20, 20);
    }

    public void onClick(DialogInterface dialogInterface, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            clockingMethodSettings.setFingerprintDevice(chosenDevice);
        }
    }

    public void onSelect(String macAddress) {
        clockingMethodSettings.setFingerprintDeviceMacAddress(macAddress);
    }
}
