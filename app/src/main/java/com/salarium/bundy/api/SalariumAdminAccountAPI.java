package com.salarium.bundy.api;

import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/** Class managing requests to Salarium admin account API
 *
 * @author Neil Marion dela Cruz
 */
public class SalariumAdminAccountAPI extends SalariumAPIConnection {
    private AdminAccountSettings adminAccountSettings;

    public SalariumAdminAccountAPI(AdminAccountSettings adminAccountSettings) {
        this.adminAccountSettings = adminAccountSettings;
    }

    /** Register a Salarium admin account into the device
     *
     * @param email                 Salarium login
     * @param password              Salarium password
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public JSONObject registerAdminAcount(String email, String password)
        throws IOException, JSONException {

        setActionPath(
            Values.API.ServerEndPoints.Actions.REGISTER_ADMIN_ACCOUNT_ACTION);
        setRequestData(Values.API.Keys.EMAIL_KEY, email);
        setRequestData(Values.API.Keys.PASSWORD_KEY, password);

        return request(Values.API.ServerEndPoints.RequestMethods.POST);
    }

    /** Register a company as the company on the device
     *
     * @param companyId             companyId
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public JSONObject registerAdminCompany(String companyId)
        throws IOException, JSONException {

        setActionPath(
            Values.API.ServerEndPoints.Actions.REGISTER_ADMIN_COMPANY
        );
        setRequestData(Values.API.Keys.COMPANY_ID_KEY, companyId);
        setRequestData(
            Values.API.Keys.TOKEN_KEY,
            adminAccountSettings.getAccountToken()
        );

        return request(Values.API.ServerEndPoints.RequestMethods.POST);
    }
}
