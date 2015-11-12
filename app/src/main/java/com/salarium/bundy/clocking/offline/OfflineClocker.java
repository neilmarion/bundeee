package com.salarium.bundy.clocking.offline;

import android.app.Activity;
import android.view.View;
import com.salarium.bundy.persistence.EmployeeDatabasePersistence;
import com.salarium.bundy.persistence.TimeRecordDatabasePersistence;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.sound.SoundBoard;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.util.Time;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;
import com.salarium.bundy.view.MessageBoardExpiringV2;
import org.json.JSONException;
import org.json.JSONObject;

/** Class that creates handles the creation of time records in the device database
 *  Only works in the V2 User Experience
 * @author Neil Marion dela Cruz
 */
public class OfflineClocker implements View.OnClickListener {
    private Activity activity;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private MethodsStateManager methodsStateManager;
    private SoundBoard soundBoard;
    private String currentEmployeeCompanyId;
    private String currentEmployeeFullName;
    private String currentEmployeeId;
    private TimeRecordDatabasePersistence timeRecordDatabasePersistence;
    private ViewManager viewManager;
    private MessageBoardExpiringV2 messageBoard = null;
    private AdminAccountSettings adminAccountSettings;

    public OfflineClocker(
        Activity activity,
        ViewManager viewManager,
        MethodsStateManager methodsStateManager
    ) {
        this.activity = activity;
        this.viewManager = viewManager;
        this.methodsStateManager = methodsStateManager;
        employeeDatabasePersistence = new EmployeeDatabasePersistence(activity);
        timeRecordDatabasePersistence = new TimeRecordDatabasePersistence(activity);
        soundBoard = new SoundBoard(activity);
        adminAccountSettings = new AdminAccountSettings(new SimplePersistence(activity), activity);
    }

    /** Initiate the insertion of time records; Asks user whether clock-in or clock-out
     *
     * @param employeeId             employee id
     */
    public void clock(String employeeId) {
        JSONObject employeeData =
            getEmployeeData(employeeId);
            try {
                currentEmployeeFullName =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeFullName = "employee " + employeeId;
            }

            try {
                currentEmployeeCompanyId =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeCompanyId = adminAccountSettings.getChosenCompanyId();
            }
        currentEmployeeId = employeeId;
        viewManager.getMessageReceiver().receiveDecisionMessage(
            Values.Icons.USER,
            currentEmployeeFullName,
            Values.Messages.Neutral.OFFLINE_MODE_CLOCKING_ACTION_QUESTION,
            Values.Labels.CLOCK_IN,
            Values.Labels.CLOCK_OUT,
            States.OfflineMode.Choice.CHOSEN_CLOCKIN,
            States.OfflineMode.Choice.CHOSEN_CLOCKOUT,
            this
        );
    }

    /** Initiate the insertion of clock-in time records
     *
     * @param employeeId             employee id
     */
    public void clockIn(String employeeId) {
        soundBoard.playClockInSoundEffect();
        JSONObject employeeData = getEmployeeData(employeeId);
        String message = "";
        String timeToday = Time.getHumanDateForToday();
            try {
                currentEmployeeFullName =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeFullName = "employee " + employeeId;
            }

            try {
                currentEmployeeCompanyId =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeCompanyId = adminAccountSettings.getChosenCompanyId();;
            }
        timeRecordDatabasePersistence.addTimeRecord(
            employeeId,
            currentEmployeeCompanyId,
            String.valueOf(States.OfflineMode.TimeAction.CLOCK_IN),
            timeToday
        );
        message =
            Values.Messages.Neutral.CLOCKED_IN + "\n" + timeToday;
        messageBoard = (MessageBoardExpiringV2) viewManager.getMessageReceiver().receiveExpiringPositiveMessage(
            Values.Icons.USER,
            currentEmployeeFullName,
            message,
            Values.Labels.OK,
            States.CLOCKING_SUCCESS,
            methodsStateManager,
            3
        );
    }

    /** Initiate the insertion of clock-out time records
     *
     * @param employeeId             employee id
     */
    public void clockOut(String employeeId) {
        soundBoard.playClockOutSoundEffect();
        JSONObject employeeData = getEmployeeData(employeeId);
        String message = "";
        String timeToday = Time.getHumanDateForToday();

            try {
                currentEmployeeFullName =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeFullName = "employee " + employeeId;
            }

            try {
                currentEmployeeCompanyId =
                    employeeData.getString(
                        Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                    );
            } catch (JSONException e) {
                currentEmployeeCompanyId = adminAccountSettings.getChosenCompanyId();
            }

        timeRecordDatabasePersistence.addTimeRecord(
            employeeId,
            currentEmployeeCompanyId,
            String.valueOf(States.OfflineMode.TimeAction.CLOCK_OUT),
            timeToday
        );
        message =
            Values.Messages.Neutral.CLOCKED_OUT + "\n" + timeToday;
        messageBoard = (MessageBoardExpiringV2) viewManager.getMessageReceiver().receiveExpiringPositiveMessage(
            Values.Icons.USER,
            currentEmployeeFullName,
            message,
            Values.Labels.OK,
            States.CLOCKING_SUCCESS,
            methodsStateManager,
            3
        );
    }

    public void onClick(View v) {
        String message = "";
        String timeToday = Time.getHumanDateForToday();
        System.out.println("tag: " + v.getTag());
        switch ((Integer) v.getTag()) {
            case States.OfflineMode.Choice.CHOSEN_CLOCKIN:
                soundBoard.playClockInSoundEffect();
                timeRecordDatabasePersistence.addTimeRecord(
                    currentEmployeeId,
                    currentEmployeeCompanyId,
                    String.valueOf(States.OfflineMode.TimeAction.CLOCK_IN),
                    timeToday
                );
                message =
                    Values.Messages.Neutral.CLOCKED_IN + "\n" + timeToday;
                messageBoard = (MessageBoardExpiringV2) viewManager.getMessageReceiver().receiveExpiringPositiveMessage(
                    Values.Icons.USER,
                    currentEmployeeFullName,
                    message,
                    Values.Labels.OK,
                    States.CLOCKING_SUCCESS,
                    methodsStateManager,
                    3);
            break;
            case States.OfflineMode.Choice.CHOSEN_CLOCKOUT:
                soundBoard.playClockOutSoundEffect();
                timeRecordDatabasePersistence.addTimeRecord(
                    currentEmployeeId,
                    currentEmployeeCompanyId,
                    String.valueOf(States.OfflineMode.TimeAction.CLOCK_OUT),
                    timeToday
                );
                message =
                    Values.Messages.Neutral.CLOCKED_OUT + "\n" + timeToday;
                messageBoard = (MessageBoardExpiringV2) viewManager.getMessageReceiver().receiveExpiringPositiveMessage(
                    Values.Icons.USER,
                    currentEmployeeFullName,
                    message,
                    Values.Labels.OK,
                    States.CLOCKING_SUCCESS,
                    methodsStateManager,
                    3);
            break;
        }
    }

    private JSONObject getEmployeeData(String employeeId) {
       JSONObject employeeData = null;
        try {
            return employeeDatabasePersistence.getEmployee(employeeId);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return employeeData;
        }
    }

    public void stopExpiringMessageReceiverCountdown() {
        if (messageBoard != null) {
            System.out.println("stop clocker");
            messageBoard.stopCountdown();
            messageBoard = null;
        }
    }
}
