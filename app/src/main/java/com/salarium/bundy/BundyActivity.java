package com.salarium.bundy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import com.salarium.bundy.clocking.method.MethodManager;
import com.salarium.bundy.config.ConfigManager;
import com.salarium.bundy.enrollment.EnrollmentActivity;
import com.salarium.bundy.menu.MenuManager;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.persistence.TimeRecordDatabasePersistence;
import com.salarium.bundy.settings.SettingsActivity;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.state.ClockingStateManager;
import com.salarium.bundy.sync.Syncer;
import com.salarium.bundy.system.ScheduledRebooter;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;
import com.salarium.bundy.state.MethodsStateManager;
import com.salarium.bundy.state.MethodsStateManagerImplementation;

/** Main Activity
 *
 * @author Neil Marion dela Cruz
 */
public class BundyActivity extends Activity {
    /** UX Version */
    public int uxVersion = ConfigManager.V2;
    public MenuManager menuManager;
    public MethodManager methodManager;
    public ScheduledRebooter scheduledRebooter;
    public SettingsManager settingsManager;
    public Syncer syncer;
    public ViewManager viewManager;
    public boolean onCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setVolumeControlStream(AudioManager.STREAM_SYSTEM);
        setContentView(ConfigManager.getBundyActivityLayout(uxVersion));
        StrictMode.ThreadPolicy policy =
            new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setActionBarTransparent();
        settingsManager = new SettingsManager(this);
        viewManager = new ViewManager(this);
        methodManager = new MethodManager(this);
        menuManager =
            new MenuManager(
                getApplicationContext(),
                this,
                viewManager,
                settingsManager
            );
        ConfigManager configManager =
            new ConfigManager(
                uxVersion,
                this,
                settingsManager,
                viewManager,
                methodManager
            );
        viewManager.setConfigManager(configManager);
        methodManager.setConfigManager(configManager);
        MethodsStateManagerImplementation clockingStateManager = null;
        try {
            clockingStateManager = configManager.getClockingStateManager();
            methodManager.setMethodsStateManagerImplementation(
                clockingStateManager
            );
        } catch (ConfigManager.NoMethodsStateManagerCreatedException e) {
            e.printStackTrace(System.out);
            viewManager.showErrorDialog(
                Values.Messages.Error.NO_METHODS_STATE_MANAGER_SET
            );
        }
        scheduledRebooter = new ScheduledRebooter(this);
        syncer = new Syncer(this);
        ((MethodsStateManager) clockingStateManager).setSyncer(syncer);
        syncer.run();
        onCreate = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bundy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
            Handle action bar item clicks here
            The action bar will automatically handle clicks on the Home/Up button,
            so long as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();
        menuManager.launchActivity(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
            TODO: Instead of passing an activity_class_name for every
            Activity callback, try using Parceleable to pass the SettingsManager
            or any objects to the called Activity
        */
        if (data.getStringExtra("activity_class_name").equals(SettingsActivity.class.getName())) {
            methodManager.resetupMethods();
            scheduledRebooter = new ScheduledRebooter(this);
        } else if (data.getStringExtra("activity_class_name").equals(EnrollmentActivity.class.getName())) {
            scheduledRebooter = new ScheduledRebooter(this);
        }
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    private void setActionBarTransparent() {
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("pause");
        methodManager.closeDevices();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resume");
        if (!onCreate) {
            methodManager.restartMethods();
        }
        onCreate = false;
    }
}
