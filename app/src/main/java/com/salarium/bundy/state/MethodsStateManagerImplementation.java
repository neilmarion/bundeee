package com.salarium.bundy.state;

public interface MethodsStateManagerImplementation {
    public void stepSuccess(String employeeId);
    public void stepFailure(String errorMessage);
    public void start();
}
