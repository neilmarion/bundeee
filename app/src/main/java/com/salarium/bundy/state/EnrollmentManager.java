package com.salarium.bundy.state;

import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.config.ConfigManager;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.BundyAlertDialog;
import com.salarium.bundy.view.DecisionNonExpiringDismissDialog;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class EnrollmentManager
    extends MethodsStateManager
    implements DialogInterface.OnClickListener {
    private DecisionNonExpiringDismissDialog askToEnrollAnotherUser;

    public EnrollmentManager(Activity activity,
        SettingsManager settingsManager,
        MethodManager methodManager,
        ViewManager viewManager) {

        super(activity, settingsManager, methodManager, viewManager);
        methodManager.setupMethods();
        startWithoutRestart();
    }

    public void startWithoutRestart() {
        stepCount = 0;
        methodManager.enrollUserByMethodIndex(stepCount);
    }

    public void start() {
        stepCount = 0;
        if (canStart()) {
            methodManager.resetupMethods();
            methodManager.enrollUserByMethodIndex(stepCount);
        } else {
            viewManager.showErrorDialog(
                Values.Messages.Error.INCOMPLETE_SETTINGS);
        }
    }

    // first step needs employee Identification such as the employee id
    public void stepSuccess(String employeeId) {
        int status = SalariumAPIConnection.FAIL;
        JSONObject response = null;
        stepCount++;

        if (stepCount < methodManager.methods.size()) {
            try {
                response = findEmployee(employeeId);
                status = response.getInt(Values.API.Keys.STATUS_KEY);
                if (status == SalariumAPIConnection.SUCCESS) {
                    methodManager.enrollUserByMethodIndex(
                        stepCount,
                        (JSONObject) response.get(Values.API.Keys.EMPLOYEE_KEY)
                    );
                } else {
                    stepFailure(
                        Values.Messages.Error.ENROLLMENT_FAILURE_EMPLOYEE_NOT_FOUND
                    );
                }
            } catch (IOException e) {
                e.printStackTrace(System.out); // remove
                stepFailure(
                    Values.Messages.Error.NETWORK_PROBLEM
                );
            } catch (JSONException e) {
                e.printStackTrace(System.out); // remove
                stepFailure(
                    Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM
                );
            }
        } else {
             askToEnrollAnotherUser =
                new DecisionNonExpiringDismissDialog(
                    activity,
                    Values.Labels.YES,
                    Values.Labels.NO,
                    Values.Messages.Neutral.ENROLL_ANOTHER_EMPLOYEE_QUESTION,
                    Values.Icons.QUESTION,
                    States.ENROLL_ANOTHER_QUESTION,
                    this);
            askToEnrollAnotherUser.showDialog();
        }
    }

    public void stepFailure(String message) {
        NegativeNonExpiringDismissDialog employeeNotFoundDialog =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.OK,
                message,
                Values.Icons.EXCLAMATION,
                States.ENROLLMENT_FAILURE,
                this);
        employeeNotFoundDialog.showDialog();
    }

    public Activity getEnrollmentActivity() {
        return activity;
    }

    public void onDismiss(DialogInterface dialog) {
        switch(((BundyAlertDialog) dialog).getState()) {

        }
    }

    public void onClick(DialogInterface dialog, int which) {
        switch(((BundyAlertDialog) dialog).getState()) {
            case States.ENROLLMENT_FAILURE:
                start();
                break;
            case States.ENROLL_ANOTHER_QUESTION:
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    start();
                } else {
                    activity.finish();
                }

                break;
        }
    }
}
