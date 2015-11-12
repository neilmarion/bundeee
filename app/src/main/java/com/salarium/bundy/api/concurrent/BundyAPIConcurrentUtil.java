package com.salarium.bundy.api.concurrent;

import android.os.Handler;
import com.salarium.bundy.api.BundyAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Runnable;
import org.json.JSONObject;

/** Class used for calling the Salarium bundy API concurrently and repeatedly
 *
 * @author Neil Marion dela Cruz
 */
public class BundyAPIConcurrentUtil {
    private static final Logger log =
        Logger.getLogger(BundyAPIConcurrentUtil.class.getName());
    private boolean stopGettingData = false;
    private AdminAccountSettings adminAccountSettings;
    private BundyAPI bundyAPI;
    private BundyAPIConcurrentUtilCallbackImplementation bundyAPIConcurrentUtilCallbackImplementation;
    private String employeeID;
    private final static int SYNC_DELAY = 3000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            getDataFromServer();
        }
    };

    public BundyAPIConcurrentUtil(
        AdminAccountSettings adminAccountSettings,
        BundyAPIConcurrentUtilCallbackImplementation bundyAPIConcurrentUtilCallbackImplementation
    ) {
        this.adminAccountSettings = adminAccountSettings;
        bundyAPI = new BundyAPI(adminAccountSettings);
        this.bundyAPIConcurrentUtilCallbackImplementation =
            bundyAPIConcurrentUtilCallbackImplementation;
    }

    /** Begin getting employee data from server
     *
     * @param employeeId    company employee ID
     */
    public void getData(String employeeID) {
        this.employeeID = employeeID;
        this.stopGettingData = false;
        runnable.run();
    }

    /** Stop checking employee data from server
     *
     * @param employeeId    company employee ID
     */
    public void stop() {
        this.stopGettingData = true;
    }

    private void getDataFromServer() {
        JSONObject employeeData = null;
        if (!stopGettingData) {
            try {
                employeeData = bundyAPI.getBundyStatus(employeeID);
                System.out.println("DATA FROM SERVER: " + employeeData);
                if (employeeData != null && employeeData.length() > 0 && employeeData.getInt(Values.API.Keys.STATUS_KEY) != 0) { // possibly remove
                    bundyAPIConcurrentUtilCallbackImplementation.
                        receiveEmployeeData(employeeData);
                    stop();
                } else {
                    handler.postDelayed(runnable, SYNC_DELAY);
                }
            } catch (Exception e) {
                System.out.println("here");
                log.log(Level.SEVERE, e.toString(), e);
                e.printStackTrace(System.out); // remove
                handler.postDelayed(runnable, SYNC_DELAY);
            }
        }
    }
}
