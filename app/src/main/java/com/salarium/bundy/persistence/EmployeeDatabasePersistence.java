package com.salarium.bundy.persistence;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.salarium.bundy.values.*;
import org.json.JSONObject;
import org.json.JSONException;

/** Database persistence class intended for employee data DB operations
 * @author Neil Marion dela Cruz
 */
public class EmployeeDatabasePersistence extends DatabasePersistence {
    private SQLiteDatabase db;

    public EmployeeDatabasePersistence(Context context) {
        super(context);
    }

    /** Determines whether or not an employee is existing in the database
     * @param employeeId             employee id
     * @param employeeCompanyId      employee id
     */
    public boolean isEmployeeExisting(
        String employeeId
    ) {
        db = openDB();
        Cursor cursor = db.query(
            Values.Persistence.Database.TABLE_NAME_EMPLOYEES,
            new String[] {
                Values.Persistence.Database.EMPLOYEE_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
            },
            Values.Persistence.Database.EMPLOYEE_ID_COLUMN + "=? ",
            new String[] { employeeId }, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    /** Get employee data stored in the database
     * @param employeeId             employee id
     */
    public JSONObject getEmployee(
        String employeeId
    ) {
        db = openDB();
        Cursor cursor = db.query(
            Values.Persistence.Database.TABLE_NAME_EMPLOYEES,
            new String[] {
                Values.Persistence.Database.EMPLOYEE_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
            },
            Values.Persistence.Database.EMPLOYEE_ID_COLUMN + "=? ",
            new String[] { employeeId }, null, null, null, null);
        if (cursor.getCount() > 0) {
            JSONObject employee = new JSONObject();
            if (cursor.moveToFirst()) {
                String employeeCompanyId = cursor.getString(
                    cursor.getColumnIndex(
                        Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                    )
                );

                String employeeFullName = cursor.getString(
                    cursor.getColumnIndex(
                        Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN
                    )
                );

                try {
                    employee.put(
                        Values.Persistence.Database.EMPLOYEE_ID_COLUMN,
                        employeeId
                    );

                    employee.put(
                        Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
                        employeeCompanyId
                    );
                    employee.put(
                        Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN,
                        employeeFullName
                    );
                } catch (JSONException e) {
                    e.printStackTrace(System.out);
                }
            }
            cursor.close();
            db.close();
            return employee;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    /** Insert employee data into the database if not yet existing, if it is update
     * @param employeeId             employee id
     * @param employeeCompanyId      employee company ID
     * @param employeeFullName       employee full name
     * @param fingerprint            fingerprint data
     */
    public void addOrUpdateEmployee(
        String employeeId,
        String employeeCompanyId,
        String employeeFullName,
        byte[] fingerprint
    ) {
        if (isEmployeeExisting(employeeId)) {
            updateEmployeeFingerprintByEmployeeId(employeeId, employeeFullName, fingerprint);
        } else {
            addEmployee(
                employeeId, employeeCompanyId, employeeFullName, fingerprint
            );
        }
    }

    /** Insert employee data into the database
     * @param employeeId             employee id
     * @param employeeCompanyId      employee company ID
     * @param employeeFullName       employee full name
     * @param fingerprint            fingerprint data
     */
    public void addEmployee(
        String employeeId,
        String employeeCompanyId,
        String employeeFullName,
        byte[] fingerprint
    ) {
        db = openDB();
        ContentValues values = new ContentValues();
        values.put(
            Values.Persistence.Database.EMPLOYEE_ID_COLUMN, employeeId
        );
        values.put(
            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
            employeeCompanyId
        );
        values.put(
            Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN,
            employeeFullName
        );
        values.put(
            Values.Persistence.Database.FINGERPRINT_COLUMN, fingerprint
        );
        long result =
            db.insert(
                Values.Persistence.Database.TABLE_NAME_EMPLOYEES, null, values
            );
        if (result == -1) {
            System.out.println("Error inserting database entry");
        } else {
            System.out.println("Successful at inserting database entry");
        }
        db.close();
    }

    /** Get employee fingerprint data
     * @param employeeId             employee id
     */
    public byte[] getEmployeeFingerprintByEmployeeId(String employeeId) {
        db = openDB();
        Cursor cursor = db.query(
            Values.Persistence.Database.TABLE_NAME_EMPLOYEES,
            new String[] { Values.Persistence.Database.FINGERPRINT_COLUMN },
            Values.Persistence.Database.EMPLOYEE_ID_COLUMN + "=?",
            new String[] { employeeId }, null, null, null, null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            cursor.close();
            db.close();
            return null;
        }
        byte[] result = cursor.getBlob(0);
        cursor.close();
        db.close();
        return result;
    }

    /** Update fingerprint data of employee
     * @param employeeId             employee id
     * @param fingerprint            fingerprint data
     */
    public int updateEmployeeFingerprintByEmployeeId(
        String employeeId,
        String employeeFullName,
        byte[] fingerprint) {

        db = openDB();
        ContentValues values = new ContentValues();
        values.put(
            Values.Persistence.Database.FINGERPRINT_COLUMN, fingerprint
        );
        values.put(
            Values.Persistence.Database.EMPLOYEE_FULL_NAME_COLUMN, employeeFullName
        );
        int result =
            db.update(
                Values.Persistence.Database.TABLE_NAME_EMPLOYEES,
                values,
                Values.Persistence.Database.EMPLOYEE_ID_COLUMN + " = ?",
                new String[] { employeeId }
            );

        db.close();
        return result;
    }
}
