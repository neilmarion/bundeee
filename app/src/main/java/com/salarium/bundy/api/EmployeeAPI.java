package com.salarium.bundy.api;

import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/** Class managing requests to Salarium employee API
 *
 * @author Neil Marion dela Cruz
 */
public class EmployeeAPI extends SalariumAPIConnection {
    private AdminAccountSettings adminAccountSettings;

    public EmployeeAPI(AdminAccountSettings adminAccountSettings) {
        this.adminAccountSettings = adminAccountSettings;
    }

    /** Find the employee using the company employee id
     *
     * @param employeeId            company employee id
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public JSONObject findEmployee(String employeeId)
        throws IOException, JSONException {

        setActionPath(Values.API.ServerEndPoints.Actions.FIND_EMPLOYEE_ACTION);
        setRequestData(Values.API.Keys.EMPLOYEE_ID_KEY, employeeId);
        setRequestData(Values.API.Keys.COMPANY_ID_KEY,
            adminAccountSettings.getChosenCompanyId());
        setRequestData(Values.API.Keys.TOKEN_KEY,
            adminAccountSettings.getAccountToken());

        return request(Values.API.ServerEndPoints.RequestMethods.GET);
    }
}
