package com.salarium.bundy.clocking.method.device;

import android.os.Handler;

/** Class implemented by the different fingerprint device classes
 *
 * @author Neil Marion dela Cruz
 */
public interface FingerprintDeviceImplementation {
    // fingerprintAPIHandler messages
    public static final int REGISTER = 1;
    public static final int VERIFY = 2;

    // fingerprintProcessHandler messages
    public static final int REGISTER_SUCCESS = 1;
    public static final int REGISTER_FAIL = 2;
    public static final int SHOW_EXPECTING_INPUT = 3;
    public static final int SHOW_CURRENTLY_VALIDATING = 4;
    public static final int SHOW_RESULT = 5;
    public static final int NO_FINGERPRINT_REGISTERED = 6;
    public static final int VERIFY_RESULT = 7;
    public static final int TERMINATE = 0;

    /** Registering an employee fingerprint
     *
     * @param employeeId             employee id
     * @param employeeCompanyId      employee company id
     * @param employeeFullName       employee company id
     */
    public void register(
        String employeeId,
        String employeeCompanyId,
        String employeeFullName
    );

    /** Verifying employee fingerprint
     *
     * @param employeeId             employee id
     */
    public void verify(String employeeId);
    public void setFingerprintProcessHandler(Handler fingerprintProcessHandler);
    public void close();
    public void reconnect();
}
