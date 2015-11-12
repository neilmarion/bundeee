package com.salarium.bundy.clocking.method;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.SimpleNumberInputViewCallback;
import com.salarium.bundy.view.ViewManager;
import com.salarium.bundy.view.MessageReceiver;
import com.salarium.bundy.view.SimpleNumberInputViewV2;
import org.json.JSONObject;

/** Class implementing the employee ID method for clocking
 *  The employee ID method is a clocking method that requires an employee
 *  ID for either identification or verification
 *
 * @author Neil Marion dela Cruz
 */
public class EmployeeIDMethodV2
    extends Method
    implements SimpleNumberInputViewCallback,
                UserIdentification,
                UserVerification
    {

    private SimpleNumberInputViewV2 simpleNumberInputViewV2;
    private String employeeIdToCompare = null;

    public EmployeeIDMethodV2(
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
        drawSimpleNumberInputViewV2();
    }

    /** Verify the user with the employee id
     *
     * @param employeeId    company employee ID
     */
    public void identifyUser() {}

    /** Verify the user with the employee id
     *
     * @param employeeId    company employee ID
     */
    public void verifyUser(JSONObject employeeData) {}

    /** Verify the user with the employee data
     *
     * @param employeeData    employee data
     */
    public void verifyUser(String employeeId) {}

    /** Enroll user
     *
     */
    public void enrollUser() {}

    /** Enroll user with employee data if this is not the first clocking method set by the admin
     *
     */
    public void enrollUser(JSONObject userDataFromServer) {}

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

    /** Disable the method
     *
     */
    public void disable() {
        simpleNumberInputViewV2.disable();
    }

    /** Remove the view
     *
     */
    public void destroy() {
        try {
        ((ViewGroup) simpleNumberInputViewV2.getParent()).
            removeView(simpleNumberInputViewV2);
        } catch (Exception e) {

        }
    }

    public void recreate() {
        destroy();
        drawSimpleNumberInputViewV2();
    }

    /** Show the simple number input view
     *
     */
    private void drawSimpleNumberInputViewV2() {
        ViewGroup view =
            (ViewGroup) ((MethodsStateManager) methodsStateManagerImplementation).
                getActivity().findViewById(R.id.main_left);

        simpleNumberInputViewV2 = viewManager.createSimpleNumberInputViewV2(
                this, Values.Messages.Neutral.ENTER_EMPLOYEE_ID);

        showMessage();
        view.addView(simpleNumberInputViewV2);
    }

    /** Show the first message
     *
     */
    private void showMessage() {
        viewManager.getMessageReceiver().receiveMessage(
            Values.Icons.INFO,
            Values.Messages.Neutral.ENTER_EMPLOYEE_ID,
            ""
        );
    }

    /** Close all the devices used by this method
     *
     */
    public void closeDevices() {}
}
