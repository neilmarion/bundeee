package com.salarium.bundy.enrollment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.config.ConfigManager;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.EnrollmentManager;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

/** Activity class for enrolling employees
 *
 * @author Neil Marion dela Cruz
 */
public class EnrollmentActivity extends Activity {
    public SettingsManager settingsManager;
    public EnrollmentManager enrollmentManager;
    public MethodManager methodManager;
    public ViewManager viewManager;
    private final int transitionDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = new SettingsManager(this);
        viewManager = new ViewManager(this);
        methodManager = new MethodManager(this);
        ConfigManager configManager =
            new ConfigManager(
                ConfigManager.V1,
                this,
                settingsManager,
                viewManager,
                methodManager
            );
        methodManager.setConfigManager(configManager);
        try {
            methodManager.setMethodsStateManagerImplementation(
                configManager.getEnrollmentStateManager()
            );
        } catch (ConfigManager.NoMethodsStateManagerCreatedException e) {
            e.printStackTrace(System.out);
            viewManager.showErrorDialog(
                Values.Messages.Error.NO_METHODS_STATE_MANAGER_SET
            );
        }
        viewManager.setConfigManager(configManager);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_enrollment);
        setActionBarTransparent();
        createBackgroundColorTransition();
        Intent intent = new Intent();
        intent.putExtra(
            Values.General.ACTIVITY_CLASS_NAME, this.getClass().getName()
        );
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onDestroy() {
        methodManager.closeDevices();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        System.out.println("back pressed");
        Intent intent = new Intent();
        intent.putExtra(
            Values.General.ACTIVITY_CLASS_NAME, getClass().getName());
        setResult(RESULT_OK, intent);
        methodManager.closeDevices();
        super.onBackPressed();
    }

    private void setActionBarTransparent() {
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    private void createBackgroundColorTransition() {
        TransitionDrawable transition =
            (TransitionDrawable) findViewById(
                R.id.activity_enrollment).getBackground();
        transition.startTransition(transitionDuration);
    }
}
