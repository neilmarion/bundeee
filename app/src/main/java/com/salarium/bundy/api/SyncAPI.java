package com.salarium.bundy.api;

import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import java.util.HashMap;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;

/** Class that manages api connections to the Salarium sync API
 *
 * @author Neil Marion dela Cruz
 */
public class SyncAPI extends SalariumAPIConnection {
    private AdminAccountSettings adminAccountSettings;

    public SyncAPI(AdminAccountSettings adminAccountSettings) {
        this.adminAccountSettings = adminAccountSettings;
    }

    /** Sync time records to the server
     *
     * @param timeRecords   Time records stored in the app database
     * @return              Response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public JSONObject syncTimeRecordsToServer(
        JSONObject timeRecords
    ) throws IOException, JSONException {
        setActionPath(
            Values.API.ServerEndPoints.Actions.SYNC_TIME_RECORDS_ACTION
        );
        setRequestData(Values.API.Keys.TOKEN_KEY,
            adminAccountSettings.getAccountToken()
        );
        setRequestData(Values.API.Keys.RECORDS_KEY, timeRecords.toString());

        return request(Values.API.ServerEndPoints.RequestMethods.POST);
    }
}
