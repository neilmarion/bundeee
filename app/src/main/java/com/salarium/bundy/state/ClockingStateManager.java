package com.salarium.bundy.state;

import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.api.EmployeeAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.net.ConnectionStatus;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.util.Time;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.PositiveNonExpiringDismissDialog;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import org.json.JSONObject;

public class ClockingStateManager
    extends MethodsStateManager
    implements DialogInterface.OnClickListener {

    private ConnectionStatus connectionStatus;

    public ClockingStateManager(Activity activity,
        SettingsManager settingsManager,
        MethodManager methodManager,
        ViewManager viewManager) {

        super(activity, settingsManager, methodManager, viewManager);
        viewManager.createClockAndDateViewInBackground();
        connectionStatus = new ConnectionStatus(activity);
        setOnClickListener();
    }

    // when screen is clicked, start with the clocking
    public void setOnClickListener() {
        View layout = activity.findViewById(R.id.activity_bundy);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canStart()) {
                    start();
                } else {
                    viewManager.showErrorDialog(
                        Values.Messages.Error.INCOMPLETE_SETTINGS);
                }
            }
        });
    }

    public void start() {
        stepCount = 0;
        methodManager.resetupMethods();
        methodManager.identifyUserByMethodIndex(stepCount);
    }

    public void stepSuccess(String employeeId) {
        int status = SalariumAPIConnection.FAIL;
        JSONObject response = null;
        stepCount++;

        try {
            // this needs to be dried-up
            if (connectionStatus.isConnected()) {
                response = findEmployee(employeeId);
                status = response.getInt(Values.API.Keys.STATUS_KEY);

                if (status == SalariumAPIConnection.SUCCESS) {
                    JSONObject employee = response;

                    if (stepCount < methodManager.methods.size()) {
                        methodManager.verifyUserByMethodIndex(
                            stepCount,
                            (JSONObject) response.get(Values.API.Keys.EMPLOYEE_KEY));
                    } else {
                        clock(employeeId);
                    }
                } else {
                    stepFailure(Values.Messages.Error.EMPLOYEE_NOT_FOUND_ERROR);
                }
            } else {
                if (stepCount < methodManager.methods.size()) {
                    methodManager.verifyUserByMethodIndex(
                        stepCount, employeeId
                    );
                } else {
                    clock(employeeId);
                }
            }



        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void stepFailure(String message) {
        NegativeNonExpiringDismissDialog stepFailure =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.OK,
                message,
                Values.Icons.EXCLAMATION,
                States.GENERAL_ERROR,
                this);
        stepFailure.showDialog();
    }

    public void clock(String employeeId) {
        try {
            if (connectionStatus.isConnected()) {
                JSONObject result = bundyAPI.clockByEmployeeId(employeeId);
                if ((Integer) result.get(Values.API.Keys.STATUS_KEY) ==
                    SalariumAPIConnection.SUCCESS) {

                    if ((Integer) result.get(Values.API.Keys.BUNDY_STATUS_KEY) ==
                        Values.API.Flags.CLOCKED_IN) {

                        PositiveNonExpiringDismissDialog clockingBundyAlertDialog =
                            new PositiveNonExpiringDismissDialog(
                                activity,
                                Values.Labels.OK,
                                result.get(
                                    Values.API.Keys.EMPLOYEE_FULL_NAME_KEY).
                                        toString() + "\n" +
                                        Values.Messages.Neutral.CLOCKED_IN + " " +
                                        Time.convertUnixTimeToHumanDate(
                                            result.get(Values.API.Keys.TIME_IN_KEY).toString()),
                                Values.Icons.CHECK,
                                States.EMPLOYEE_CLOCKED,
                                this);
                        clockingBundyAlertDialog.showDialog();
                    } else {
                        PositiveNonExpiringDismissDialog clockingBundyAlertDialog =
                            new PositiveNonExpiringDismissDialog(
                                activity,
                                Values.Labels.OK,
                                result.get(
                                    Values.API.Keys.EMPLOYEE_FULL_NAME_KEY).
                                        toString() + "\n" +
                                        Values.Messages.Neutral.CLOCKED_OUT + " " +
                                        Time.convertUnixTimeToHumanDate(
                                            result.get(Values.API.Keys.TIME_OUT_KEY).toString()),
                                Values.Icons.CHECK,
                                States.EMPLOYEE_CLOCKED,
                                this);
                        clockingBundyAlertDialog.showDialog();
                    }
                } else {
                    stepFailure(Values.Messages.Error.CLOCKING_EMPLOYEE_NOT_FOUND);
                }
            } else {
                // offline
                // create time record here
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void onClick(DialogInterface dialog, int which) {}
}
