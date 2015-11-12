package com.salarium.bundy.net;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.salarium.bundy.api.ConnectionStatusAPI;

/** Class determining the connection status to the Salarium server
 * @author Neil Marion dela Cruz
 */
public class ConnectionStatus {
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetwork;
    private ConnectionStatusAPI connectionStatusAPI;

    public ConnectionStatus(Activity activity) {
        connectivityManager = (ConnectivityManager) activity.getSystemService(
            Activity.CONNECTIVITY_SERVICE
        );
        connectionStatusAPI = new ConnectionStatusAPI();
    }

    /**
    * Determines if can be connected to the server
    */
    public boolean isConnected() {
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected =
            activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {
            if (connectionStatusAPI.canConnectToServer()) {
                return true;
            }
        }
        return false;
    }
}
