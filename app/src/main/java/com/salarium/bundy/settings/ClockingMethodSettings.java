package com.salarium.bundy.settings;

import android.app.Activity;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import java.util.ArrayList;
import java.util.Arrays;

/** Settings class handling clocking methods
 * @author Neil Marion dela Cruz
 */
public class ClockingMethodSettings extends Settings {
    private final Activity activity;
    private final SimplePersistence simplePersistence;

    public static final int EMPLOYEE_ID_METHOD =
        R.string.pref_clocking_settings_identification_employee_id_id;
    public static final int RFID_METHOD =
        R.string.pref_clocking_settings_identification_rfid_id;
    public static final int QRCODE_METHOD =
        R.string.pref_clocking_settings_identification_qrcode_id;

    public static final int FINGERPRINT_METHOD =
        R.string.pref_clocking_settings_verification_fingerprint_id;
    public static final int PHOTO_CAPTURE_METHOD =
        R.string.pref_clocking_settings_verification_photo_capture_id;
    public static final int VOICE_RECOGNITION_METHOD =
        R.string.pref_clocking_settings_verification_voice_recognition_id;
    public static final int PIN_ID_METHOD =
        R.string.pref_clocking_settings_verification_pin_id;

    public static final ArrayList<Integer> IDENTIFICATION_IDS
        = new ArrayList<Integer>(Arrays.asList(EMPLOYEE_ID_METHOD,
                                                RFID_METHOD,
                                                QRCODE_METHOD));

    public static final ArrayList<Integer> VERIFICATION_IDS
        = new ArrayList<Integer>(
            Arrays.asList(
                FINGERPRINT_METHOD,
                PHOTO_CAPTURE_METHOD,
                VOICE_RECOGNITION_METHOD,
                PIN_ID_METHOD)
            );

    public static final ArrayList<Integer> METHOD_IDS
        = new ArrayList<Integer>(
            Arrays.asList(
                EMPLOYEE_ID_METHOD,
                RFID_METHOD,
                QRCODE_METHOD,
                FINGERPRINT_METHOD,
                PHOTO_CAPTURE_METHOD,
                VOICE_RECOGNITION_METHOD,
                PIN_ID_METHOD)
            );

    public ClockingMethodSettings(
        SimplePersistence simplePersistence, Activity activity
    ) {
        this.simplePersistence = simplePersistence;
        this.activity = activity;
    }

    @Override
    public boolean complete() {
        return checkIfAtLeastOneIdentificationMethodIsSet();
    }

    public boolean isClockingMethodActivated(int id) {
        if(simplePersistence.getBooleanPersistence(activity.getString(id))) {
            return true;
        }
        return false;
    }

    public boolean isIdentificationMethod(String key) {
        for (int s : IDENTIFICATION_IDS) {
            if (activity.getString(s).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyOneIdentificationSet() {
        int count = 0;
        for (int s : IDENTIFICATION_IDS) {
            if (simplePersistence.getBooleanPersistence(activity.getString(s))) {
                count++;
            }
        }
        if (count == 1) {
            return true;
        }
        return false;
    }

    private boolean checkIfAtLeastOneIdentificationMethodIsSet() {
        for (int s : IDENTIFICATION_IDS) {
            if(simplePersistence.getBooleanPersistence(activity.getString(s))) {
                return true;
            }
        }
        return false;
    }

    public void setFingerprintDevice(String device) {
        simplePersistence.persistString(
            activity.getString(
                R.string.pref_clocking_settings_verification_fingerprint_device_id
            ),
            device
        );
    }

    public String getFingerprintDevice() {
        String device = simplePersistence.getPersistence(
            activity.getString(R.string.pref_clocking_settings_verification_fingerprint_device_id)
        );

        if (device.isEmpty()) {
            return activity.getString(R.string.pref_clocking_settings_verification_fingerprint_device_a370_id);
        }

        return simplePersistence.getPersistence(
            activity.getString(R.string.pref_clocking_settings_verification_fingerprint_device_id)
        );
    }

    public void setFingerprintDeviceMacAddress(String deviceMacAddress) {
        simplePersistence.persistString(
            activity.getString(R.string.pref_clocking_settings_verification_fingerprint_device_mac_address_id),
            deviceMacAddress
        );
    }

    public String getFingerprintDeviceMacAddress() {
        return simplePersistence.getPersistence(
            activity.getString(R.string.pref_clocking_settings_verification_fingerprint_device_mac_address_id)
        );
    }
}
