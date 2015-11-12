package com.salarium.bundy.system;

import com.salarium.bundy.extra.Clock;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.SystemRebootTimeSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.PositiveNonExpiringDismissDialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import java.lang.Runnable;

public class ScheduledRebooter {
    private String timeToReboot; // 24-hour format
    private Handler checkerHandler = new Handler();
    private Handler rebootHandler = new Handler();
    private Clock clock;
    private Activity activity;

    private Runnable checkRunnable = new Runnable() {
        public void run() {
            check();
        }
    };

    private Runnable rebootRunnable = new Runnable() {
        public void run() {
            reboot();
        }
    };

    public ScheduledRebooter(
        Activity activity) {

        SystemRebootTimeSettings systemRebootTimeSettings =
            new SystemRebootTimeSettings(
                new SimplePersistence(activity), activity);
        this.activity = activity;
        this.timeToReboot = systemRebootTimeSettings.getRebootTimeString();
        System.out.println("time to reboot: " + timeToReboot);
        clock = new Clock();
        checkRunnable.run();
    }

    private void check() {
        if (isTimeToReboot()) {
            (new PositiveNonExpiringDismissDialog(
                    activity,
                    Values.Labels.OK,
                    Values.Messages.Neutral.REBOOTING,
                    Values.Icons.EXCLAMATION)).showDialog();
            rebootHandler.postDelayed(
                rebootRunnable, Values.Durations.SHOW_REBOOTING_MESSAGE_DIALOG);
        }

        checkerHandler.postDelayed(checkRunnable,
            Values.Durations.ONE_THOUSAND_MILLISECONDS);
    }

    public void reboot() {
        Intent startActivityIntent = new Intent(activity, activity.getClass());
        PendingIntent pendingIntent =
            PendingIntent.getActivity(
                activity,
                Values.IDs.PENDING_BUNDY_ACTIVITY_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr =
            (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(
            AlarmManager.RTC,
            System.currentTimeMillis()
                + Values.Durations.ONE_THOUSAND_MILLISECONDS,
            pendingIntent);
        System.out.println(Values.Messages.Neutral.REBOOTING);
        System.exit(0);
    }

    private boolean isTimeToReboot() {
        if (timeToReboot.equals(clock.getCurrentTime())) {
            return true;
        }

        return false;
    }
}
