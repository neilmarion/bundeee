package com.salarium.bundy.values;

// TODO: Change to BundyAlertDialog states
public class States {
    public static final int EMPLOYEE_NOT_FOUND_ERROR = 1;
    public static final int SHOW_WHOS_EMPLOYEE = 2;
    public static final int FINGERPRINT_REGISTRATION_SUCCESS = 3;
    public static final int ENROLL_ANOTHER_QUESTION = 4;
    public static final int NO_FINGERPRINT_REGISTERED = 5;
    public static final int EXPECTING_TO_PLACE_FINGER_ON_SCANNER = 6;
    public static final int ENROLLMENT_FAILURE = 7;
    public static final int VERIFYING_FINGERPRINT = 8;
    public static final int GENERAL_ERROR = 9;
    public static final int EMPLOYEE_CLOCKED = 10;
    public static final int CLOCKING_FAILURE = 11;
    public static final int CLOCKING_SUCCESS = 12;
    public static final int BLUETOOTH_OFF = 13;
    public static final int BLUETOOTH_DEVICE_NOT_SET = 14;

    public class OfflineMode {
        public class Choice {
            public static final int CHOSEN_CLOCKIN = 1;
            public static final int CHOSEN_CLOCKOUT = 2;
        }

        public class TimeAction {
            public static final int CLOCK_IN = 1;
            public static final int CLOCK_OUT = 2;
        }
    }
}
