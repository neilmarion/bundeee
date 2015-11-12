package com.salarium.bundy.config;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.clocking.method.EmployeeIDMethod;
import com.salarium.bundy.clocking.method.EmployeeIDMethodV2;
import com.salarium.bundy.clocking.method.FingerprintMethod;
import com.salarium.bundy.clocking.method.FingerprintMethodV2;
import com.salarium.bundy.clocking.method.MethodImplementation;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.ClockingStateManager;
import com.salarium.bundy.state.ClockingStateManagerV2;
import com.salarium.bundy.state.EnrollmentManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.MessageBoardManagerV2;
import com.salarium.bundy.view.MessageReceiver;
import com.salarium.bundy.view.ViewManager;

/** Class that generates the dependencices depending on the UX version being used
 * @author Neil Marion dela Cruz
 */
public class ConfigManager {
    public static final int V1 = 1;
    public static final int V2 = 2;
    protected int version = 1;
    protected Activity activity;
    protected SettingsManager settingsManager;
    protected ViewManager viewManager;
    protected MethodManager methodManager;

    public ConfigManager(
        int version,
        Activity activity,
        SettingsManager settingsManager,
        ViewManager viewManager,
        MethodManager methodManager) {

        this.version = version;
        this.activity = activity;
        this.settingsManager = settingsManager;
        this.viewManager = viewManager;
        this.methodManager = methodManager;
    }

    /** Returns the intended main layout
     *
     * @param version UX version
     */
    public static int getBundyActivityLayout(int version) {
        int layout;
        switch(version) {
            default:
                layout = R.layout.activity_bundy;
                break;
            case V2:
                layout = R.layout.activity_bundy_v2;
                break;
        }

        return layout;
    }

    /** Returns the intended clocking state manager
     */
    public MethodsStateManagerImplementation getClockingStateManager()
        throws NoMethodsStateManagerCreatedException
    {

        MethodsStateManagerImplementation clockingStateManager = null;
        switch(version) {
            default:
                clockingStateManager = new ClockingStateManager(
                    activity,
                    settingsManager,
                    methodManager,
                    viewManager);
            break;
            case V2:
                clockingStateManager = new ClockingStateManagerV2(
                    activity,
                    settingsManager,
                    methodManager,
                    viewManager);
            break;
        }

        if (clockingStateManager == null) {
            throw new NoMethodsStateManagerCreatedException(
                Values.API.ExceptionMessages.
                    NO_METHOD_STATE_MANAGER_IMPLEMENTATION_CREATED
            );
        }

        return clockingStateManager;
    }

    /** Returns the intended enrollment state manager
     */
    public MethodsStateManagerImplementation getEnrollmentStateManager()
        throws NoMethodsStateManagerCreatedException {

        MethodsStateManagerImplementation enrollmentManager = null;
        switch(version) {
            default:
               enrollmentManager
                    = new EnrollmentManager(
                        activity,
                        settingsManager,
                        methodManager,
                        viewManager);
            case V2:
                // none yet
            break;
        }

        if (enrollmentManager == null) {
            throw new NoMethodsStateManagerCreatedException(
                Values.API.ExceptionMessages.
                    NO_METHOD_STATE_MANAGER_IMPLEMENTATION_CREATED
            );
        }

        return enrollmentManager;
    }

    public class NoMethodsStateManagerCreatedException extends Exception {
        public NoMethodsStateManagerCreatedException(String message) {
            super(message);
        }
    }

    /** Returns the intended employee ID method
     */
    public MethodImplementation createEmployeeIDMethod(
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        BundyAPI bundyAPI
    ) {

        MethodImplementation employeeIDMethod = null;

        switch (version) {
            default:
                employeeIDMethod =
                    new EmployeeIDMethod(
                        methodsStateManagerImplementation,
                        viewManager,
                        settingsManager,
                        bundyAPI);
                break;
            case V2:
                employeeIDMethod =
                    new EmployeeIDMethodV2(
                        methodsStateManagerImplementation,
                        viewManager,
                        settingsManager,
                        bundyAPI);
                break;
        }

        return employeeIDMethod;
    }

    /** Returns the intended fingerprint method
     */
    public MethodImplementation createFingerprintMethod(
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        BundyAPI bundyAPI
    ) {

        MethodImplementation fingerprintMethod = null;

        switch (version) {
            default:
                fingerprintMethod =
                    new FingerprintMethod(
                        methodsStateManagerImplementation,
                        viewManager,
                        settingsManager,
                        bundyAPI);
                break;
            case V2:
                fingerprintMethod =
                    new FingerprintMethodV2(
                        methodsStateManagerImplementation,
                        viewManager,
                        settingsManager,
                        bundyAPI);
                break;
        }

        return fingerprintMethod;
    }

    /** Returns the intended message receiver
     */
    public MessageReceiver createMessageReceiver() {
        MessageReceiver messageReceiver = null;

        switch (version) {
            default:
                break;
            case V2:
                messageReceiver =
                    new MessageBoardManagerV2(activity);
                break;
        }

        return messageReceiver;
    }
}
