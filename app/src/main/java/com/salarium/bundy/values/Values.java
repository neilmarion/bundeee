/*
* Final values being used by com.salarium.bundy.api.* classes
*/

package com.salarium.bundy.values;

public class Values {
    public class General {
        public static final String ACTIVITY_CLASS_NAME = "activity_class_name";
    }

    public class Messages {
        public class Error {
            public static final String CLOCKING_EMPLOYEE_NOT_FOUND =
                "Clocking failed. Employee not found";
            public static final String SOMETHING_WENT_WRONG =
                "Something went wrong. Please try again";
            public static final String EMPLOYEE_ID_NOT_CONSISTENT =
                "The employee ID you entered is inconsistent" +
                " from the previous enrollment steps. Please try again";
            public static final String INCOMPLETE_SETTINGS =
                "Please complete the device settings before using the device for clocking in";
            public static final String FINGERPRINT_MATCH_FAILED =
                "Clocking process cancelled. Fingerprint doesn't match the employee's enrolled fingerprint. Please try again";
            public static final String FINGERPRINT_REGISTRATION_FAILED =
                "Fingerprint registration failed. Please try again";
            public static final String EMPLOYEE_NOT_FOUND_ERROR =
                "Employee not found";
            public static final String NO_FINGERPRINT_REGISTERED =
                "You still have not enrolled a fingerprint";
            public static final String ENROLLMENT_FAILURE_EMPLOYEE_NOT_FOUND
                = "Enrollment Failure. Employee not found";
            public static final String WRONG_DEVICE_PIN
                = "Wrong device pin";
            public static final String WRONG_EMAIL_OR_PASSWORD
                = "Wrong email or password. Please try again.";
            public static final String NETWORK_PROBLEM
                = "Network problem";
            public static final String DATA_RETURNED_BY_SERVER_PROBLEM
                = "Something wrong with server's response";
            public static final String ONLY_ONE_IDENTIFICATION_LEFT
                = "You cannot unset this method.\nAt least one identification method must be set.";
            public static final String BAD_DATA_ERROR
                = "Bad data in the device database.";
            public static final String NO_METHODS_STATE_MANAGER_SET
                = "No methods state manager set";
            public static final String TRY_AGAIN
                = "Please try again";
            public static final String BLUETOOTH_OFF
                = "The fingerprint method needs the bluetooth device turned on";
            public static final String BLUETOOTH_DEVICE_NOT_SET_ERROR
                = "Please select bluetooth device";
        }

        public class Neutral {
            public static final String PROCESS_CANCELLED =
                "Process cancelled";
            public static final String ENROLLMENT_CANCELLED =
                "Enrollment process cancelled";
            public static final String FINGERPRINT_REGISTERED =
                "Fingerprint registered!";
            public static final String VERIFYING_FINGERPRINT =
                "Verifying fingerprint";
            public static final String VERIFYING_FINGERPRINT_DO_NOT_REMOVE =
                "Do not remove your finger on the scanner.\nVerifying fingerprint";
            public static final String EXPECTING_TO_PLACE_FINGER_ON_SCANNER =
                "Please place your finger tip over the fingerprint scanner";
            public static final String TAP_TO_BEGIN_CLOCK =
                "Tap anywhere to begin clocking-in or clocking-out";
            public static final String ENTER_EMPLOYEE_ID =
                "Please enter your employee ID";
            public static final String ENTER_EMPLOYEE_ID_ENROLLMENT =
                "Please enter the employee ID to begin enrollment";
            public static final String ENROLL_ANOTHER_EMPLOYEE_QUESTION =
                "Enrollment is successful. Do you want to enroll another user?";
            public static final String CLOCKING_CANCELLED =
                "Clocking process cancelled";
            public static final String ACCOUNT_REGISTERED_ON_DEVICE =
                "Salarium account registered into the device. Please choose the desired company";
            public static final String ACCOUNT_NOT_REGISTERED_ON_DEVICE =
                "There's something wrong with the entered email and password. Please try again";
            public static final String CLOCKED_IN =
                "CLOCKED-IN";
            public static final String CLOCKED_OUT =
                "CLOCKED-OUT";
            public static final String REBOOTING =
                "Applying recent updates. Please wait for a few seconds until the app reboots...";
            public static final String NO_PAIRED_DEVICES =
                "No paired devices.";
            public static final String EMPLOYEE =
                "Employee";
            public static final String OFFLINE_MODE_CLOCKING_ACTION_QUESTION =
                "Choose an action";
        }
    }

    public class Labels {
        public static final String OK = "OK";
        public static final String DEVICE_PIN =
            "Device PIN";
        public static final String CANCEL_ENROLLMENT = "CANCEL ENROLLMENT";
        public static final String YES = "YES";
        public static final String NO = "NO";
        public static final String CANCEL = "CANCEL";
        public static final String SAVE = "SAVE";
        public static final String CLOCK_IN = "CLOCK IN";
        public static final String CLOCK_OUT = "CLOCK OUT";
    }

    public class API { // for com.salarium.bundy.api
        public class ServerEndPoints {
            public static final String SERVER_DOMAIN_NAME =
                //"http://dev.vpc.salarium.com";
                //"http://v2.salarium.local";
                "https://salarium.com";

            public class Actions {
                public static final String CLOCK_BY_EMPLOYEE_ID_ACTION =
                    "api/bundy/clock";
                public static final String FIND_EMPLOYEE_ACTION =
                    "api/employee/find";
                public static final String REGISTER_ADMIN_ACCOUNT_ACTION =
                    "api/bundy_admin/register_device";
                public static final String CHECK_SERVER_CONNECTION_ACTION =
                    "api/connection/check";
                public static final String SYNC_TIME_RECORDS_ACTION =
                    "api/bundy_sync/sync_time_records";
                public static final String REGISTER_ADMIN_COMPANY =
                    "api/bundy_admin/register_admin_company";
                public static final String GET_BUNDY_STATUS_ACTION =
                    "api/bundy/get_bundy_status";
            }

            public class RequestMethods {
                public static final int POST = 1;
                public static final int GET = 2;
            }
        }

        public class Keys {
            public static final String EMPLOYEE_ID_KEY =
                "employee_id";
            public static final String COMPANY_ID_KEY =
                "company_id";
            public static final String TOKEN_KEY =
                "token";
            public static final String RECORDS_KEY =
                "records";
            public static final String EMPLOYEE_FULL_NAME_KEY =
                "employee_full_name";
            public static final String BUNDY_STATUS_KEY =
                "bundy_status";
            public static final String EMAIL_KEY =
                "email";
            public static final String PASSWORD_KEY =
                "password";
            public static final String STATUS_KEY =
                "status";
            public static final String EMPLOYEE_KEY =
                "employee";
            public static final String COMPANIES_KEY =
                "companies";
            public static final String COMPANY_NAME_KEY =
                "company_name";
            public static final String ACCOUNT_TOKEN_KEY =
                "account_token";
            public static final String TIME_IN_KEY =
                "time_in";
            public static final String TIME_OUT_KEY =
                "time_out";
            public static final String EMPLOYEE_FIRST_NAME_KEY =
                "employee_first_name";
            public static final String EMPLOYEE_LAST_NAME_KEY =
                "employee_last_name";
        }

        public class Flags {
            public static final int CLOCKED_IN = 1;
            public static final int CLOCKED_OUT = 2;
        }

        public class ExceptionMessages {
            public static final String INCOMPLETE_PARAMETERS =
                "Action path was not specified. Before calling" +
                "SalariumAPIConnection.request() call" +
                "SalariumAPIConnection.setActionPath() to set the action path";
            public static final String
                NO_METHOD_STATE_MANAGER_IMPLEMENTATION_CREATED =
                    "No MethodStateManagerImplementation created";
        }
    }

    public class Clocking {
        public class Method {
            public class Types {
                public static final int IDENTIFICATION = 1;
                public static final int VERIFICATION = 2;
                public static final int IDENTIFICATION_OR_VERIFICATION = 3;
            }
        }
    }

    // fontawesome
    public class Icons {
        public static final String EXCLAMATION = "{fa-exclamation-circle}";
        public static final String QUESTION = "{fa-question-circle}";
        public static final String CHECK = "{fa-check-circle}";
        public static final String HANDS_DOWN = "{fa-hand-o-down}";
        public static final String GEARS = "{fa-gears}";
        public static final String USER = "{fa-user}";
        public static final String INFO = "{fa-info-circle}";
    }

    public class Persistence {
        public class Database {
            public static final String DATABASE_NAME = "salarium_bundy";

            public static final String TABLE_NAME_EMPLOYEES = "employees";
            public static final String EMPLOYEE_ID_COLUMN = "company_employee_id";
            public static final String EMPLOYEE_COMPANY_ID_COLUMN = "company_id";
            public static final String EMPLOYEE_FULL_NAME_COLUMN = "employee_full_name";
            public static final String FINGERPRINT_COLUMN = "fingerprint";
            public static final String ID_COLUMN = "id";

            public static final String TABLE_NAME_TIME_RECORDS = "time_records";
            public static final String TIME_ACTION_ID_COLUMN = "time_action_id";
            public static final String CREATED_AT_COLUMN = "created_at";

            public class Changes {
                public class Version2 {
                    public static final int DATABASE_VERSION = 2;
                }

                public class Version3 {
                    public static final int DATABASE_VERSION = 3;
                    public static final String TABLE_CREATE_QUERY_EMPLOYEES =
                                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_EMPLOYEES + " (" +
                                EMPLOYEE_ID_COLUMN + " TEXT, " +
                                EMPLOYEE_COMPANY_ID_COLUMN + " TEXT, " +
                                EMPLOYEE_FULL_NAME_COLUMN + " TEXT, " +
                                FINGERPRINT_COLUMN + " BLOB);";

                    public static final String TABLE_CREATE_QUERY_TIME_RECORDS =
                                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TIME_RECORDS + " (" +
                                ID_COLUMN + " INTEGER PRIMARY KEY, " +
                                EMPLOYEE_ID_COLUMN + " TEXT, " +
                                EMPLOYEE_COMPANY_ID_COLUMN + " TEXT, " +
                                TIME_ACTION_ID_COLUMN + " TEXT, " +
                                CREATED_AT_COLUMN + " TEXT);";

                    public static final String ALTER_EMPLOYEES_TABLE_ADD_EMPLOYEE_ID =
                        "ALTER TABLE " + TABLE_NAME_EMPLOYEES + " ADD COLUMN " +
                        EMPLOYEE_ID_COLUMN + " TEXT;";
                    public static final String ALTER_EMPLOYEES_TABLE_ADD_COMPANY_ID =
                        "ALTER TABLE " + TABLE_NAME_EMPLOYEES + " ADD COLUMN " +
                        EMPLOYEE_COMPANY_ID_COLUMN + " TEXT;";
                    public static final String ALTER_EMPLOYEES_TABLE_ADD_FULL_NAME =
                        "ALTER TABLE " + TABLE_NAME_EMPLOYEES + " ADD COLUMN " +
                        EMPLOYEE_FULL_NAME_COLUMN + " TEXT;";
                    public static final String UPDATE_EMPLOYEES_TABLE_COPY_EMPLOYEE_ID =
                        "UPDATE " + TABLE_NAME_EMPLOYEES + " SET " +  EMPLOYEE_ID_COLUMN + " = employee_id";

                    /*
                    public static final String[] CHANGES_ON_CREATE = {
                        TABLE_CREATE_QUERY_EMPLOYEES,
                        TABLE_CREATE_QUERY_TIME_RECORDS
                    };

                    public static final String[] CHANGES_ON_UPGRADE = {
                        "ALTER TABLE " + TABLE_NAME_EMPLOYEES + " ADD COLUMN" +
                        EMPLOYEE_ID_COLUMN + " TEXT;",
                        "ALTER TABLE " + TABLE_NAME_EMPLOYEES + " ADD COLUMN" +
                        EMPLOYEE_FULL_NAME_COLUMN + " TEXT;",
                        TABLE_CREATE_QUERY_TIME_RECORDS
                    };
                    */
                }
            }
        }
    }

    public class Durations {
        public static final int CLOCKING_STARTOVER = 3000;
        public static final int ONE_THOUSAND_MILLISECONDS = 1000;
        public static final int SHOW_REBOOTING_MESSAGE_DIALOG = 5000;
    }

    public class IDs {
        public static final int PENDING_BUNDY_ACTIVITY_INTENT_ID = 123456;
    }

    public class Config {
        public static final String AUTO_REBOOT_TIME = "12:52:00";
    }

    public class SoundFXFiles {
        public static final String BUTTON1 = "button.mp3";
        public static final String POSITIVE = "positive.mp3";
        public static final String NEGATIVE = "negative.mp3";
    }
}
