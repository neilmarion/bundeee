/*
*   Manages the step-by-step process of different interacting Clocking methods.
*   Whether it's for clocking or enrollment.
*/
package com.salarium.bundy.state;

import com.salarium.bundy.values.*;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.view.ViewManager;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.api.EmployeeAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import java.io.IOException;
import org.json.JSONException;
import com.salarium.bundy.sync.Syncer;


import org.json.JSONObject;
import android.app.Activity;
import android.view.View;

public abstract class MethodsStateManager
    implements MethodsStateManagerImplementation, View.OnClickListener {

    protected SettingsManager settingsManager;
    protected MethodManager methodManager;
    protected Activity activity;
    protected ViewManager viewManager;
    protected BundyAPI bundyAPI;
    protected EmployeeAPI employeeAPI;
    protected int stepCount = 0; // goes back to zero
    protected Syncer syncer;

    public MethodsStateManager(
        Activity activity,
        SettingsManager settingsManager,
        MethodManager methodManager,
        ViewManager viewManager) {

        this.activity = activity;
        this.settingsManager = settingsManager;
        this.methodManager = methodManager;
        this.viewManager = viewManager;

        bundyAPI =
            new BundyAPI(this.settingsManager.getAdminAccountSettings());
        employeeAPI =
            new EmployeeAPI(this.settingsManager.getAdminAccountSettings());

        methodManager.setMethodsStateManagerImplementation(this);
        methodManager.setViewManager(viewManager);
        methodManager.setSettingsManager(settingsManager);
        methodManager.setBundyAPI(bundyAPI);
    }

    protected JSONObject findEmployee(String employeeId)
        throws IOException, JSONException {

        return employeeAPI.findEmployee(employeeId);
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean canStart() {
        return settingsManager.isComplete();
    }

    public MethodManager getMethodManager() {
        return methodManager;
    }

    public void onClick(View v) {

    }

    public void setSyncer(Syncer syncer) {
        this.syncer = syncer;
    }

    public Syncer getSyncer() {
        return syncer;
    }
}
