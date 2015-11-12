package com.salarium.bundy.state;

import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.api.concurrent.BundyAPIConcurrentUtilCallbackImplementation;
import com.salarium.bundy.api.concurrent.BundyAPIConcurrentUtil;
import com.salarium.bundy.api.EmployeeAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.clocking.offline.OfflineClocker;
import com.salarium.bundy.net.ConnectionStatus;
import com.salarium.bundy.persistence.EmployeeDatabasePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.sound.SoundBoard;
import com.salarium.bundy.util.Time;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.MessageReceiver;
import com.salarium.bundy.view.ViewManager;
import com.salarium.bundy.view.MessageBoardExpiringV2;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import java.lang.Thread;
import java.util.ArrayList;
import org.json.JSONObject;
import java.io.IOException;
import org.json.JSONException;

public class ClockingStateManagerV2
    extends MethodsStateManager
    implements BundyAPIConcurrentUtilCallbackImplementation {

    private ConnectionStatus connectionStatus;
    private OfflineClocker offlineClocker;
    private SoundBoard soundBoard;
    private JSONObject employeeData;
    private BundyAPIConcurrentUtil bundyAPIConcurrentUtil;
    private BundyAPI bundyAPI;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private String companyId;
    private MessageBoardExpiringV2 messageBoardCancelled = null;

    public ClockingStateManagerV2(Activity activity,
        SettingsManager settingsManager,
        MethodManager methodManager,
        ViewManager viewManager) {

        super(activity, settingsManager, methodManager, viewManager);
        viewManager.createClockAndDateViewInMainLeft();
        connectionStatus = new ConnectionStatus(activity);
        offlineClocker = new OfflineClocker(activity, viewManager, this);
        soundBoard = new SoundBoard(activity);
        bundyAPIConcurrentUtil = new BundyAPIConcurrentUtil(
            settingsManager.getAdminAccountSettings(), this
        );
        bundyAPI = new BundyAPI(
            settingsManager.getAdminAccountSettings()
        );
        employeeDatabasePersistence = new EmployeeDatabasePersistence(activity);
        companyId = settingsManager.getAdminAccountSettings().getChosenCompanyId();
        methodManager.setupMethods();
        startWithoutRestart();
    }

    public void startWithoutRestart() {
        stepCount = 0;
        employeeData = null;
        //bundyAPIConcurrentUtil.stop();
        methodManager.identifyUserByMethodIndex(stepCount);
    }

    public void start() {
        stepCount = 0;
        employeeData = null;
        bundyAPIConcurrentUtil.stop();
        methodManager.restartMethods();
        methodManager.identifyUserByMethodIndex(stepCount);
    }

    public void stepSuccess(String employeeId) {
        if (employeeData == null) {
            bundyAPIConcurrentUtil.getData(employeeId);
        }

        methodManager.disableMethodByIndex(stepCount);
        stepCount++;
        try {
            if (employeeDatabasePersistence.isEmployeeExisting(
                    employeeId
                )
            ) {
                if (stepCount < methodManager.methods.size()) {
                    methodManager.verifyUserByMethodIndex(
                        stepCount, employeeId
                    );
                } else {
                    clock(employeeId);
                }
            } else {
                stepFailure(Values.Messages.Error.EMPLOYEE_NOT_FOUND_ERROR);
            }
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace(System.out);
            stepFailure(Values.Messages.Error.TRY_AGAIN);
        }
    }

    public void stepFailure(String message) {
        soundBoard.playErrorSoundEffect();
        methodManager.disableMethodByIndex(stepCount);
        System.out.println("stepFailure");
        messageBoardCancelled = (MessageBoardExpiringV2) viewManager.getMessageReceiver().receiveExpiringNegativeMessage(
            Values.Icons.EXCLAMATION,
            message,
            "",
            Values.Labels.OK,
            States.CLOCKING_FAILURE,
            this,
            5
        );
    }

    public void clock(String employeeId) {
        /*
        int status = 0;
        try {
            status = employeeData.getInt(Values.API.Keys.STATUS_KEY);
        } catch (JSONException e) {
            e.printStackTrace(System.out);
        }
        */
        try {
            if (employeeData != null && employeeData.length() > 0 && employeeData.getInt(Values.API.Keys.STATUS_KEY) != 0) {
                    if ((Integer) employeeData.get(Values.API.Keys.BUNDY_STATUS_KEY) == Values.API.Flags.CLOCKED_IN) {
                        offlineClocker.clockIn(employeeId);
                    } else {
                        offlineClocker.clockOut(employeeId);
                    }
                    getSyncer().syncRecords();

            } else {
                offlineClocker.clock(employeeId);
            }
        } catch (JSONException e) {
            e.printStackTrace(System.out);
            stepFailure(Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM);
        }
    }

    @Override
    public void onClick(View v) {
        switch ((Integer) v.getTag()) {
            case States.CLOCKING_SUCCESS:
            case States.CLOCKING_FAILURE:
                offlineClocker.stopExpiringMessageReceiverCountdown();
                if (messageBoardCancelled != null) {
                    System.out.println("stopping countdown");
                    messageBoardCancelled.stopCountdown();
                    messageBoardCancelled = null;
                }
                start();
            break;
        }
    }

    public void receiveEmployeeData(JSONObject employeeData) {
        System.out.println("employeeData: " + employeeData);
        this.employeeData = employeeData;
    }
}
