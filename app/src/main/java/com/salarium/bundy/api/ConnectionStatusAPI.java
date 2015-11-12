package com.salarium.bundy.api;

import com.salarium.bundy.values.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONObject;

import com.salarium.bundy.settings.AdminAccountSettings;

/** Class managing requests to Salarium bundy connection status API
 *
 * @author Neil Marion dela Cruz
 */
public class ConnectionStatusAPI extends SalariumAPIConnection {
    private static final Logger log =
        Logger.getLogger(ConnectionStatusAPI.class.getName());

    /** Check if app can connect to the server
     *
     * @return Return true if app can connect to the server
     */
    public boolean canConnectToServer() {
        setActionPath(
            Values.API.ServerEndPoints.Actions.CHECK_SERVER_CONNECTION_ACTION
        );
        JSONObject response = null;
        try {
            response = request(Values.API.ServerEndPoints.RequestMethods.GET);
            if(response == null) {
                return false;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            e.printStackTrace(System.out); // remove
            return false;
        }
        return true;
    }
}
