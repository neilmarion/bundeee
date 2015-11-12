package com.salarium.bundy.clocking.method;

import android.view.View;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.SimpleNumberInputViewDialog;
import com.salarium.bundy.view.SimpleNumberInputViewCallback;
import com.salarium.bundy.view.ViewManager;
import org.json.JSONObject;

/** Class implementing the employee ID method for clocking
 *  The employee ID method is a clocking method that requires an employee
 *  ID for either identification or verification
 *
 * @author Neil Marion dela Cruz
 */
public class EmployeeIDMethod
    extends Method
    implements SimpleNumberInputViewCallback,
                UserIdentification,
                UserVerification
    {

    private SimpleNumberInputViewDialog simpleNumberInputViewDialog = null;
    private String employeeIdToCompare = null;

    public EmployeeIDMethod(
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        ViewManager viewManager,
        SettingsManager settingsManager,
        BundyAPI bundyAPI
    ) {
        super(
            Values.Clocking.Method.Types.IDENTIFICATION_OR_VERIFICATION,
            methodsStateManagerImplementation,
            viewManager,
            settingsManager,
            bundyAPI
        );
        this.simpleNumberInputViewDialog =
            viewManager.createSimpleNumberInputViewDialog(
                this, Values.Messages.Neutral.ENTER_EMPLOYEE_ID
            );
    }

    /** Begin the identification
     *
     */
    public void identifyUser() {
        simpleNumberInputViewDialog.show();
    }

    /** Verify the user with the employee data
     *
     * @param employeeData    employee data
     */
    public void verifyUser(JSONObject employeeData) {
        try {
            employeeIdToCompare =
                employeeData.getString(Values.API.Keys.EMPLOYEE_ID_KEY);
            simpleNumberInputViewDialog.show();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /** Verify the user with the employee id
     *
     * @param employeeId    company employee ID
     */
    public void verifyUser(String employeeId) {
        employeeIdToCompare = employeeId;
        simpleNumberInputViewDialog.show();
    }

    /** Enroll user
     *
     */
    public void enrollUser() {
        simpleNumberInputViewDialog.show();
    }

    /** Enroll user with employee data if this is not the first clocking method set by the admin
     *
     */
    public void enrollUser(JSONObject employeeData) {}

    /** This method works alongside with the SimpleNumberInput classes which returns the user's input received by this function
     *
     */
    public void receiveSimpleNumberInput(String number) {
        if (employeeIdToCompare != null && !employeeIdToCompare.equals(number)) {
            methodsStateManagerImplementation.stepFailure(
                Values.Messages.Error.EMPLOYEE_ID_NOT_CONSISTENT
            );
        } else {
            methodsStateManagerImplementation.stepSuccess(number);
        }
    }

    public void disable() {}
    public void destroy() {}
    public void closeDevices() {}
    public void recreate() {}
}
