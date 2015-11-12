package com.salarium.bundy.clocking.method;

import android.view.View;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.view.ViewManager;
import org.json.JSONObject;

/** Parent class of all the methods
 *
 * @author Neil Marion dela Cruz
 */
public abstract class Method
    implements MethodImplementation {

    protected BundyAPI bundy;
    protected int methodType;
    protected MethodsStateManagerImplementation methodsStateManagerImplementation;
    protected SettingsManager settingsManager;
    protected String employeeIdToCompare;
    protected ViewManager viewManager;

    public Method(
        int methodType,
        MethodsStateManagerImplementation methodsStateManagerImplementation,
        ViewManager viewManager,
        SettingsManager settingsManager,
        BundyAPI bundy
    ) {
        this.methodType = methodType;
        this.methodsStateManagerImplementation =
            methodsStateManagerImplementation;
        this.viewManager = viewManager;
        this.settingsManager = settingsManager;
        this.bundy = bundy;
    }
}
