package com.salarium.bundy.clocking.method;

import org.json.JSONObject;

/** Class for implementing clocking records used for verification of employee
 *
 * @author Neil Marion dela Cruz
 */
public interface UserVerification {
    public void verifyUser(JSONObject userDataFromServer); // using employeeId
    public void verifyUser(String employeeId); // using employeeId
    public void enrollUser(JSONObject userDataFromServer);
    public void closeDevices();
}
