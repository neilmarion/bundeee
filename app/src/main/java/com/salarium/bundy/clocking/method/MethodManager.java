package com.salarium.bundy.clocking.method;

import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.BundyActivity;
import com.salarium.bundy.clocking.method.EmployeeIDMethod;
import com.salarium.bundy.clocking.method.QRCodeMethod;
import com.salarium.bundy.clocking.method.RFIDMethod;
import com.salarium.bundy.config.ConfigManager;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import org.json.JSONObject;
import java.util.ArrayList;
import android.app.Activity;

/*
* NOTE ON CLOCKING:
* 1. An IDENTIFICATON, or IDENTIFICATION_AND_VERIFICATION method can be
*    the first method in the methods list. Whereas a VERIFICATION method cannot.
* 2. The first method in the methods list is the only method that can call
*    identifyUser(), the rest can just call verifyUser(String employeeId);
*/

/*
* NOTE ON ENROLLMENT:
* 1. An IDENTIFICATON, or IDENTIFICATION_AND_VERIFICATION method can be
*    the first method in the methods list. Whereas a VERIFICATION method cannot.
* 2. The first method in the methods list is the only method that can call
*    enrollUser(), the rest can just call enrollUser(JSONObject user);
* 3. Enrollment strictly needs to be online all the time.
*/

public class MethodManager {
    private MethodsStateManagerImplementation methodsStateManagerImplementation;
    private Activity activity;
    private ConfigManager configManager;
    private ViewManager viewManager;
    private SettingsManager settingsManager;
    private BundyAPI bundyAPI;

    public ArrayList<MethodImplementation> methods = new ArrayList();
    // the first one in the list must be an identification method

    public MethodManager(Activity activity) {
        this.activity = activity;
    }

    public void setMethodsStateManagerImplementation(
        MethodsStateManagerImplementation methodsStateManagerImplementation) {

        this.methodsStateManagerImplementation =
            methodsStateManagerImplementation;
    }

    public void resetupMethods () {
        for(MethodImplementation method : methods) {
            method.closeDevices();
            method.destroy();
            //method.recreate();
        }
        methods = new ArrayList<MethodImplementation>();
        setupMethods();
        //restartMethods();
    }

    public void restartMethods() {
        for(MethodImplementation method : methods) {
            method.recreate();
        }
    }

    public void closeDevices () {
        for(MethodImplementation method : methods) {
            method.closeDevices();
        }
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public void setBundyAPI(BundyAPI bundyAPI) {
        this.bundyAPI = bundyAPI;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public ArrayList<MethodImplementation> getMethods () {
        return methods;
    }

    // V2
    public void disableMethodByIndex(int index) {
        methods.get(index).disable();
    }

    public void identifyUserByMethodIndex(int index) {
        try {
            ((UserIdentification) getMethods().get(index)).identifyUser();
        } catch (java.lang.IndexOutOfBoundsException e) {
            e.printStackTrace(System.out);
            ((BundyActivity) activity).getMenuManager().launchActivity(R.id.action_settings);
        }
    }

    public void verifyUserByMethodIndex(int index, JSONObject employeeData) {
        ((UserVerification) getMethods().get(index)).verifyUser(employeeData);
    }

    // used on offline mode
    public void verifyUserByMethodIndex(int index, String employeeId) {
        ((UserVerification) getMethods().get(index)).verifyUser(employeeId);
    }

    public void enrollUserByMethodIndex(
        int index,
        JSONObject employee) {

        ((UserVerification) getMethods().
            get(index)).enrollUser(employee);
    }

    public void enrollUserByMethodIndex(int index) {
        ((UserIdentification) getMethods().
            get(index)).enrollUser();
    }

    public void setupMethods() {
        for (int s : ClockingMethodSettings.METHOD_IDS) {
            if (settingsManager.getClockingMethodSettings().
                isClockingMethodActivated(s)) {

                switch(s) {
                    case ClockingMethodSettings.EMPLOYEE_ID_METHOD:
                        methods.add(configManager.createEmployeeIDMethod(
                            methodsStateManagerImplementation, bundyAPI));
                        break;

                    case ClockingMethodSettings.RFID_METHOD:
                        methods.add(
                            new RFIDMethod(
                                methodsStateManagerImplementation, viewManager,
                                settingsManager, bundyAPI));
                        break;
                    case ClockingMethodSettings.QRCODE_METHOD:
                        methods.add(
                            new QRCodeMethod(
                                methodsStateManagerImplementation, viewManager,
                                settingsManager, bundyAPI));
                        break;
                    case ClockingMethodSettings.FINGERPRINT_METHOD:
                        methods.add(
                            configManager.createFingerprintMethod(
                                methodsStateManagerImplementation, bundyAPI));
                        break;
                }
            }
        }
    }
}
