package com.salarium.bundy.clocking.method;

/** Class for implementing clocking methods
 *
 * @author Neil Marion dela Cruz
 */
public interface MethodImplementation {
    public void disable();
    public void destroy();
    public void recreate();
    public void closeDevices();
}
