package com.salarium.bundy.api;

import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/** Class managing requests to Salarium bundy API
 *
 * @author Neil Marion dela Cruz
 */
public class BundyAPI extends SalariumAPIConnection {
    private AdminAccountSettings adminAccountSettings;

    public BundyAPI(AdminAccountSettings adminAccountSettings) {
        this.adminAccountSettings = adminAccountSettings;
    }

    /** Clock the employee in or out
     *
     * @param employeeId            company employee id
     * @return                      response data
     * @throws java.io.IOException
     */
    public JSONObject clockByEmployeeId(String employeeId)
        throws IOException, JSONException {

        setActionPath(
            Values.API.ServerEndPoints.Actions.CLOCK_BY_EMPLOYEE_ID_ACTION
        );
        setRequestData(Values.API.Keys.EMPLOYEE_ID_KEY, employeeId);
        setRequestData(
            Values.API.Keys.COMPANY_ID_KEY,
            adminAccountSettings.getChosenCompanyId()
        );
        setRequestData(
            Values.API.Keys.TOKEN_KEY,
            adminAccountSettings.getAccountToken()
        );

        return request(Values.API.ServerEndPoints.RequestMethods.POST);
    }

    /** Get bundy status of the employee whether it is clocked-in or clocked-out
     *
     * @param employeeId            company employee id
     * @return                      response data in JSON format e.g. {"status":1,"employee_full_name":"Neil Marion dela Cruz","employee_id":"1","bundy_status":1}
     * @throws java.io.IOException
     */
    public JSONObject getBundyStatus(
            String employeeId
        ) throws IOException, JSONException {

        setActionPath(
            Values.API.ServerEndPoints.Actions.GET_BUNDY_STATUS_ACTION);
        setRequestData(Values.API.Keys.EMPLOYEE_ID_KEY, employeeId);
        setRequestData(
            Values.API.Keys.COMPANY_ID_KEY,
            adminAccountSettings.getChosenCompanyId()
        );
        setRequestData(
            Values.API.Keys.TOKEN_KEY,
            adminAccountSettings.getAccountToken()
        );

        return request(Values.API.ServerEndPoints.RequestMethods.GET);
    }
}
