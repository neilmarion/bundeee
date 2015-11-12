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
import com.salarium.bundy.sound.SoundBoard;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.BundyAlertDialog;
import com.salarium.bundy.view.DecisionNonExpiringDismissDialog;
import com.salarium.bundy.view.MessageBoardExpiringV2;
import com.salarium.bundy.view.MessageReceiver;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import com.salarium.bundy.view.PositiveNonExpiringDismissDialog;
import com.salarium.bundy.view.ViewManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.InterruptedException;
import java.lang.Thread;

/** Class implementing the fingerprint method for clocking
 *
 * @author Neil Marion dela Cruz
 */
public class FingerprintMethodV2
    extends Method
    implements UserVerification, View.OnClickListener {

    private Activity activity;
    private boolean isCanceled = false;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private final int TOTAL_TRIES = 3;
    private FingerprintDeviceImplementation fingerprintDevice;
    private Handler fingerprintProcessHandler;
    private HandlerThread handlerThread;
    private int tries = 0;
    private JSONObject currentEmployeeData;
    private MessageBoardExpiringV2 expectingInputDialog;
    private SoundBoard soundBoard;
    private String currentEmployeeFullname = "";
    private String currentEmployeeId = "";
    private String employeeIdToCompare;

    public FingerprintMethodV2(
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
        activity =
            ((MethodsStateManager) methodsStateManagerImplementation).
                getActivity();
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
                        employeeDatabasePersistence);
        } else if (fingerprintDeviceName.equals(activity.getString(R.string.hf7000))) {
            fingerprintDevice =
                new FingerprintDeviceHF7000(
                        handlerThread.getLooper(),
                        fingerprintProcessHandler,
                        ((MethodsStateManager) methodsStateManagerImplementation).
                            getActivity(),
                        employeeDatabasePersistence);
        } else {
            // Throw exception here that no device was selected
        }

        soundBoard = new SoundBoard(
            ((MethodsStateManager) methodsStateManagerImplementation).
                getActivity()
        );
    }

    /** Verify the user with the employee data
     *
     * @param employeeData    employee data
     */
    public void verifyUser(JSONObject employeeData) {
        tries++;
        try {
            this.currentEmployeeData = employeeData;
            employeeIdToCompare =
                employeeData.getString(Values.API.Keys.EMPLOYEE_ID_KEY);
            fingerprintDevice.verify(employeeIdToCompare);
            currentEmployeeFullname =
                employeeData.getString(Values.API.Keys.EMPLOYEE_FIRST_NAME_KEY) +
                " " +
                employeeData.getString(Values.API.Keys.EMPLOYEE_LAST_NAME_KEY);
        } catch (JSONException e) {
            e.printStackTrace(System.out);
            methodsStateManagerImplementation.stepFailure(
                Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM
            );
        }
    }

    /** Verify the user with the employee id
     *
     * @param employeeId    company employee ID
     */
    public void verifyUser(String employeeId) {
        tries++;
        try {
        currentEmployeeData =
            employeeDatabasePersistence.getEmployee(employeeId);
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace(System.out);
            methodsStateManagerImplementation.
                stepFailure(Values.Messages.Error.TRY_AGAIN);
        }
        employeeIdToCompare = employeeId;
        if (currentEmployeeData != null) {
            try {
                employeeIdToCompare =
                    currentEmployeeData.getString(Values.Persistence.Database.EMPLOYEE_ID_COLUMN);
                try {
                    currentEmployeeFullname =
                        currentEmployeeData.getString(
                            Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
                        );
                } catch (JSONException e) {
                    e.printStackTrace(System.out);
                    currentEmployeeFullname = "employee " + employeeId;
                }
            } catch (JSONException e) {
                e.printStackTrace(System.out);
                methodsStateManagerImplementation.stepFailure(
                    Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM
                );
            }
            fingerprintDevice.verify(employeeIdToCompare);
        } else {
            methodsStateManagerImplementation.stepFailure(
                Values.Messages.Error.NO_FINGERPRINT_REGISTERED
            );
        }
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

    private HandlerThread getHandlerThread() {
        return new HandlerThread("handlerThread");
    }

    private void createFingerprintProcessHandler() {
        final FingerprintMethodV2 fingerprintMethod = this;
        fingerprintProcessHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FingerprintDeviceImplementation.SHOW_EXPECTING_INPUT:
                        if (tries <= 1) {
                            //expectingInputDialog = (MessageBoardExpiringV2)
                                viewManager.getMessageReceiver().receiveNegativeMessage(
                                    Values.Icons.USER,
                                    currentEmployeeFullname,
                                    Values.Messages.Neutral.
                                        EXPECTING_TO_PLACE_FINGER_ON_SCANNER,
                                    Values.Labels.CANCEL,
                                    States.EXPECTING_TO_PLACE_FINGER_ON_SCANNER,
                                    fingerprintMethod);
                                    //10);
                        } else {
                            soundBoard.playErrorSoundEffect();
                            //expectingInputDialog = (MessageBoardExpiringV2)
                                viewManager.getMessageReceiver().receiveNegativeMessage(
                                    Values.Icons.USER,
                                    currentEmployeeFullname,
                                    Values.Messages.Neutral.
                                        EXPECTING_TO_PLACE_FINGER_ON_SCANNER +
                                        "\n(" + tries + "/" + TOTAL_TRIES + ") tries",
                                    Values.Labels.CANCEL,
                                    States.EXPECTING_TO_PLACE_FINGER_ON_SCANNER,
                                    fingerprintMethod);
                                    //10);
                        }
                    break;
                    case FingerprintDeviceImplementation.SHOW_CURRENTLY_VALIDATING:
                        viewManager.getMessageReceiver().receiveNegativeMessage(
                            Values.Icons.USER,
                            currentEmployeeFullname,
                            Values.Messages.Neutral.
                                VERIFYING_FINGERPRINT,
                            Values.Labels.CANCEL,
                            States.VERIFYING_FINGERPRINT,
                            fingerprintMethod);
                        /*
                        try {
                            expectingInputDialog.stopCountdown();
                        } catch (NullPointerException e) {
                            e.printStackTrace(System.out);
                            isCanceled = true;
                        }
                        */
                    break;
                    case FingerprintDeviceImplementation.SHOW_RESULT:
                        boolean result = (Boolean) msg.obj;
                        /*
                        try {
                            expectingInputDialog.stopCountdown();
                        } catch (NullPointerException e) {
                            e.printStackTrace(System.out);
                            isCanceled = true;
                        }
                        */
                        if (result) {
                            methodsStateManagerImplementation.
                                stepSuccess(employeeIdToCompare);
                        } else {
                            if (!isCanceled) {
                                System.out.println("iscancellled=========================");
                                if (tries < TOTAL_TRIES) {
                                    verifyUser(employeeIdToCompare);
                                } else {
                                    methodsStateManagerImplementation.
                                        stepFailure(Values.Messages.Error.
                                            FINGERPRINT_MATCH_FAILED);
                                }
                            }
                        }

                    break;
                    case FingerprintDeviceImplementation.TERMINATE:
                        methodsStateManagerImplementation.
                            stepFailure(Values.Messages.Error.TRY_AGAIN);
                    break;
                    case FingerprintDeviceImplementation.NO_FINGERPRINT_REGISTERED:
                        methodsStateManagerImplementation.
                            stepFailure(
                                currentEmployeeFullname + "\n" +
                                Values.Messages.Error.NO_FINGERPRINT_REGISTERED);
                    break;
                }
            }
        };
    }

    /** Enroll user
     *
     */
    public void enrollUser(JSONObject employee) {}
    public void onClick(View v) {
        switch ((Integer) v.getTag()) {
            case States.EXPECTING_TO_PLACE_FINGER_ON_SCANNER:
            case States.VERIFYING_FINGERPRINT:
                isCanceled = true;

                //expectingInputDialog.stopCountdown();
                methodsStateManagerImplementation.
                    stepFailure(Values.Messages.Neutral.PROCESS_CANCELLED);
                break;
        }
    }

    public void recreate() {
        tries = 0;
        fingerprintDevice.reconnect();
    }
}
