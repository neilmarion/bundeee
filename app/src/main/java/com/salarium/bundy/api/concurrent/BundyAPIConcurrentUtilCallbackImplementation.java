package com.salarium.bundy.api.concurrent;

import org.json.JSONObject;

/** Class that implements receiving of an employee data from the server
 *
 * @author Neil Marion dela Cruz
 */
public interface BundyAPIConcurrentUtilCallbackImplementation {
    public void receiveEmployeeData(JSONObject employeeData);
}
