package com.salarium.bundy.clocking.method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.clocking.method.device.FingerprintDeviceA370;
import com.salarium.bundy.clocking.method.device.FingerprintDeviceHF7000;
import com.salarium.bundy.clocking.method.device.FingerprintDeviceImplementation;
import com.salarium.bundy.persistence.EmployeeDatabasePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.BundyAlertDialog;
import com.salarium.bundy.view.DecisionNonExpiringDismissDialog;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import com.salarium.bundy.view.PositiveNonExpiringDismissDialog;
import com.salarium.bundy.view.ViewManager;
import org.json.JSONObject;

/** Class implementing the fingerprint method for clocking
 *
 * @author Neil Marion dela Cruz
 */
public class FingerprintMethod
    extends Method
    implements DialogInterface.OnClickListener, UserVerification {

    private Activity activity;
    private boolean isCanceled = false;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private FingerprintDeviceImplementation fingerprintDevice;
    private Handler fingerprintProcessHandler;
    private HandlerThread handlerThread;
    private NegativeNonExpiringDismissDialog expectingInputDialog;
    private NegativeNonExpiringDismissDialog noFingerprintRegisteredDialog;
    private NegativeNonExpiringDismissDialog verifyingFingerprintDialog;
    private PositiveNonExpiringDismissDialog registerSuccessDialog;
    private String currentEmployeeCompanyId = "";
    private String currentEmployeeFullName = "";
    private String currentEmployeeId = "";
    private String employeeIdToCompare;

    public FingerprintMethod(
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        ViewManager viewManager,
        SettingsManager settingsManager,
        BundyAPI bundyAPI
    ) {
        super(
            Values.Clocking.Method.Types.VERIFICATION,
            methodsStateManagerImplementation,
            viewManager,
            settingsManager,
            bundyAPI
        );
        handlerThread = getHandlerThread();
        handlerThread.start();
        createFingerprintProcessHandler();
        activity = ((MethodsStateManager) methodsStateManagerImplementation).
            getActivity();
        createProcessDialogs();
        employeeDatabasePersistence = new EmployeeDatabasePersistence(activity);
        String fingerprintDeviceName =
            settingsManager.getClockingMethodSettings().getFingerprintDevice();
        if (fingerprintDeviceName.equals(activity.getString(R.string.a370))) {
            fingerprintDevice =
                new FingerprintDeviceA370(
                    handlerThread.getLooper(),
                    fingerprintProcessHandler,
                    ((MethodsStateManager) methodsStateManagerImplementation).
                        getActivity(),
                    employeeDatabasePersistence
                );
        } else if (fingerprintDeviceName.equals(activity.getString(R.string.hf7000))) {
            fingerprintDevice =
                new FingerprintDeviceHF7000(
                    handlerThread.getLooper(),
                    fingerprintProcessHandler,
                    ((MethodsStateManager) methodsStateManagerImplementation).
                        getActivity(),
                    employeeDatabasePersistence
                );
        } else {
            // Throw exception here that no device was selected
        }
    }

    /** Verify the user with the employee data
     *
     * @param employeeData    employee data
     */
    public void verifyUser(JSONObject employeeData) {
        try {
            employeeIdToCompare =
                employeeData.getString(Values.API.Keys.EMPLOYEE_ID_KEY);
            fingerprintDevice.verify(employeeIdToCompare);
            currentEmployeeFullName =
                employeeData.getString(Values.API.Keys.EMPLOYEE_FIRST_NAME_KEY) +
                " " +
                employeeData.getString(Values.API.Keys.EMPLOYEE_LAST_NAME_KEY);
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
        fingerprintDevice.verify(employeeIdToCompare);
    }


    private HandlerThread getHandlerThread() {
        return new HandlerThread("handlerThread");
    }

    /** Creates dialogs that will be shown in every step of the process
     *
     */
    private void createProcessDialogs() {
        expectingInputDialog =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.CANCEL,
                Values.Messages.Neutral.EXPECTING_TO_PLACE_FINGER_ON_SCANNER,
                Values.Icons.HANDS_DOWN,
                States.EXPECTING_TO_PLACE_FINGER_ON_SCANNER,
                this);

        verifyingFingerprintDialog =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.CANCEL_ENROLLMENT,
                Values.Messages.Neutral.VERIFYING_FINGERPRINT_DO_NOT_REMOVE,
                Values.Icons.GEARS,
                States.VERIFYING_FINGERPRINT,
                this);

        // if only enrollment
        registerSuccessDialog =
            new PositiveNonExpiringDismissDialog(
                activity,
                Values.Labels.OK,
                Values.Messages.Neutral.FINGERPRINT_REGISTERED,
                Values.Icons.CHECK,
                States.FINGERPRINT_REGISTRATION_SUCCESS,
                this);
    }

    /** Handles the step-by-step process of the fingerprint method
     *
     */
    private void createFingerprintProcessHandler() {
        fingerprintProcessHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FingerprintDeviceImplementation.SHOW_EXPECTING_INPUT:
                        expectingInputDialog.showDialog();
                    break;
                    case FingerprintDeviceImplementation.SHOW_CURRENTLY_VALIDATING:
                        expectingInputDialog.cancel();
                        verifyingFingerprintDialog.showDialog();
                    break;
                    case FingerprintDeviceImplementation.REGISTER_SUCCESS:
                        verifyingFingerprintDialog.cancel();
                        registerSuccessDialog.showDialog();
                        fingerprintDevice.close();
                    break;
                    case FingerprintDeviceImplementation.REGISTER_FAIL:
                        if (!isCanceled) {
                            verifyingFingerprintDialog.cancel();
                            methodsStateManagerImplementation.
                                stepFailure(Values.Messages.Error.
                                    FINGERPRINT_REGISTRATION_FAILED);
                        }
                        fingerprintDevice.close();
                    break;
                    case FingerprintDeviceImplementation.SHOW_RESULT:
                        boolean result = (Boolean) msg.obj;
                        if (result) {
                            verifyingFingerprintDialog.cancel();
                            methodsStateManagerImplementation.
                                stepSuccess(employeeIdToCompare);
                        } else {
                            if (!isCanceled) {
                                verifyingFingerprintDialog.cancel();
                                methodsStateManagerImplementation.
                                    stepFailure(Values.Messages.Error.
                                        FINGERPRINT_MATCH_FAILED);
                            }
                        }

                        fingerprintDevice.close();
                    break;
                    case FingerprintDeviceImplementation.TERMINATE:
                        methodsStateManagerImplementation.
                            stepFailure(Values.Messages.Error.TRY_AGAIN);
                        fingerprintDevice.close();
                    case FingerprintDeviceImplementation.NO_FINGERPRINT_REGISTERED:
                        methodsStateManagerImplementation.
                            stepFailure(
                                currentEmployeeFullName + "\n" +
                                Values.Messages.Error.NO_FINGERPRINT_REGISTERED);
                        fingerprintDevice.close();
                    break;
                }
            }
        };

    }

    /** Enroll user
     *
     */
    public void enrollUser(JSONObject employee) {
        String employeeFirstName = "";
        String employeeLastName = "";

        try {
            currentEmployeeId = employee.getString(
                Values.API.Keys.EMPLOYEE_ID_KEY
            );
            currentEmployeeCompanyId = employee.getString(
                Values.API.Keys.COMPANY_ID_KEY
            );
            employeeFirstName = employee.getString(
                Values.API.Keys.EMPLOYEE_FIRST_NAME_KEY
            );
            employeeLastName = employee.getString(
                Values.API.Keys.EMPLOYEE_LAST_NAME_KEY
            );
            currentEmployeeFullName = employeeFirstName + " " + employeeLastName;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        String employeeName = employeeFirstName + " " + employeeLastName;
        String message = "Hi " + employeeName +
            ", you are about to register your fingerprint.";
        String icon = "{fa-user}";

        System.out.println("employee_id: " + currentEmployeeId);
        DecisionNonExpiringDismissDialog showWhosEmployee =
            new DecisionNonExpiringDismissDialog(
                    activity,
                    Values.Labels.OK,
                    Values.Labels.CANCEL_ENROLLMENT,
                    message,
                    icon,
                    States.SHOW_WHOS_EMPLOYEE,
                    this);
        showWhosEmployee.showDialog();
    }

    /** Start enrollment process
     *
     */
    private void triggerEnrollUser(
        String employeeId, String employeeCompanyId
    ) {

        fingerprintDevice.register(
            employeeId,
            employeeCompanyId,
            currentEmployeeFullName
        );
    }

    /** Disable the method
     *
     */
    public void disable() {}

    /** Destroy the method
     *
     */
    public void destroy() {}

    /** Close all the devices used by this method
     *
     */
    public void closeDevices() {
        fingerprintDevice.close();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch(((BundyAlertDialog) dialog).getState()) {
            case States.SHOW_WHOS_EMPLOYEE:
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    triggerEnrollUser(
                        currentEmployeeId, currentEmployeeCompanyId
                    );
                } else {
                    methodsStateManagerImplementation.stepFailure(
                            Values.Messages.Neutral.ENROLLMENT_CANCELLED);
                }
                break;
            case States.FINGERPRINT_REGISTRATION_SUCCESS:
                methodsStateManagerImplementation.
                    stepSuccess(employeeIdToCompare);
                break;
            case States.ENROLL_ANOTHER_QUESTION:
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    methodsStateManagerImplementation.
                        start();
                }
                break;
            case States.EXPECTING_TO_PLACE_FINGER_ON_SCANNER:
                isCanceled = true;
                methodsStateManagerImplementation.
                    stepFailure(Values.Messages.Neutral.PROCESS_CANCELLED);
                break;
            case States.VERIFYING_FINGERPRINT:
                isCanceled = true;
                methodsStateManagerImplementation.
                    stepFailure(Values.Messages.Neutral.PROCESS_CANCELLED);
                break;
        }
    }
    public void recreate() {}
}
