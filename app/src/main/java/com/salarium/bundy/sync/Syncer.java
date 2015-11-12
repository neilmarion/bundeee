package com.salarium.bundy.sync;

import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.api.SyncAPI;
import com.salarium.bundy.net.ConnectionStatus;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.persistence.TimeRecordDatabasePersistence;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;

import android.app.Activity;
import android.os.Handler;
import java.lang.Runnable;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Syncer {
    private TimeRecordDatabasePersistence timeRecordDatabasePersistence;
    private static final Logger log = Logger.getLogger(Syncer.class.getName());
    private SyncAPI syncAPI;
    private final static int SYNC_DELAY = 30000;
    AdminAccountSettings adminAccountSettings;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            sync();
        }
    };

    ConnectionStatus connectionStatus;

    public Syncer(Activity activity) {
        connectionStatus = new ConnectionStatus(activity);
        timeRecordDatabasePersistence =
            new TimeRecordDatabasePersistence(activity);
        adminAccountSettings = new AdminAccountSettings(
            new SimplePersistence(activity), activity
        );
        syncAPI = new SyncAPI(adminAccountSettings);
    }

    public void run() {
        runnable.run();
    }

    private void sync() {
        isConnected();
        handler.postDelayed(runnable, SYNC_DELAY);
    }

    private void isConnected() {
        if (connectionStatus.isConnected()) {
            System.out.println("connected");
            syncRecords();
        } else {
            System.out.println("not connected");
        }
    }

    public void syncRecords() {
        JSONObject result = null;
        JSONObject timeRecords = timeRecordDatabasePersistence.getTimeRecordsByCompanyId(
            adminAccountSettings.getChosenCompanyId()
        );

        System.out.println(timeRecords);
        if (timeRecords.length() > 0) {
            System.out.println(
                "Found " + timeRecords.length() +
                "time records in the database. Syncing records..."
            );
            System.out.println(timeRecords);
            try {
                result = syncAPI.syncTimeRecordsToServer(timeRecords);
                result.get(Values.API.Keys.STATUS_KEY);
                System.out.println(
                    "Syncing success. Deleting records from the DB"
                );
                timeRecordDatabasePersistence.deleteAllTimeRecordsByCompanyId(
                    adminAccountSettings.getChosenCompanyId()
                );
            } catch (Exception e) {
                log.log(Level.SEVERE, e.toString(), e);
                e.printStackTrace(System.out); // remove
                System.out.println("Failed to sync time records to server"); // put to log
            }
        }
    }
}
