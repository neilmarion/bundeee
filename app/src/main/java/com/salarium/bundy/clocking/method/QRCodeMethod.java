package com.salarium.bundy.clocking.method;

import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.DemoDialogView;
import com.salarium.bundy.view.DemoDialogViewCallback;
import com.salarium.bundy.view.ViewManager;

import android.view.View;
import org.json.JSONObject;

public class QRCodeMethod
    extends Method
    implements DemoDialogViewCallback,
                UserIdentification,
                UserVerification {

    private DemoDialogView demoDialogView = null;

    public QRCodeMethod(
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        ViewManager viewManager,
        SettingsManager settingsManager,
        BundyAPI bundyAPI) {

        super(
            Values.Clocking.Method.Types.IDENTIFICATION_OR_VERIFICATION,
            methodsStateManagerImplementation,
            viewManager,
            settingsManager,
            bundyAPI);

        demoDialogView =
            viewManager.
                createDemoDialogView(this);
    }

    public void identifyUser() {
        // TODO: Remove this, this is just for demo
        demoDialogView.show();
        employeeIdToCompare = "justADummyEmployeeId";
    }

    public void verifyUser(JSONObject employeeData) {
        demoDialogView.show();
        try {
            employeeIdToCompare =
                employeeData.getString(Values.API.Keys.EMPLOYEE_ID_KEY);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void verifyUser(String employeeId) {
        demoDialogView.show();
        employeeIdToCompare = employeeId;
    }

    public void proceed() { // dummy method
        /*
        * let's say the result came in from the RFID method
        * therefore cheat by equalling employeeId = this.employeeIdToCompare
        */
        methodsStateManagerImplementation.stepSuccess(employeeIdToCompare);
    }

    public void enrollUser() {

    }

    public void enrollUser(JSONObject userDataFromServer) {

    }

    public void disable() {}
    public void destroy() {}
    public void closeDevices() {}
    public void recreate() {}
}
