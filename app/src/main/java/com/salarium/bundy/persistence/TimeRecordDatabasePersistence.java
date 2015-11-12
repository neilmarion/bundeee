package com.salarium.bundy.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.salarium.bundy.values.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Database persistence class intended for time records data DB operations
 * @author Neil Marion dela Cruz
 */
public class TimeRecordDatabasePersistence extends DatabasePersistence {
    private SQLiteDatabase db;

    public TimeRecordDatabasePersistence(Context context) {
        super(context);
    }

    public void addTimeRecord(
        String employeeId,
        String employeeCompanyId,
        String timeActionId,
        String createdAt
    ) {
        db = openDB();
        ContentValues values = new ContentValues();
        values.put(
            Values.Persistence.Database.EMPLOYEE_ID_COLUMN, employeeId);
        values.put(
            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN, employeeCompanyId);
        values.put(
            Values.Persistence.Database.TIME_ACTION_ID_COLUMN, timeActionId);
        values.put(
            Values.Persistence.Database.CREATED_AT_COLUMN, createdAt);

        long result =
            db.insert(Values.Persistence.Database.TABLE_NAME_TIME_RECORDS, null, values);
        if (result == -1) {
            System.out.println("Error inserting database entry");
        } else {
            System.out.println("Successful at inserting database entry");
        }
        db.close();
    }

    public JSONObject getTimeRecordsByCompanyId(String companyId) {
        db = openDB();
        System.out.println("company id: " + companyId);
        Cursor cursor = db.query(
            Values.Persistence.Database.TABLE_NAME_TIME_RECORDS,
            new String[] {
                Values.Persistence.Database.ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
                Values.Persistence.Database.TIME_ACTION_ID_COLUMN,
                Values.Persistence.Database.CREATED_AT_COLUMN
            },
            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN + "=?",
            new String[] { companyId }, null, null, null, null
        );

        JSONObject result = new JSONObject();
        try {
            if (cursor.moveToFirst()) {
                JSONArray ja = new JSONArray();

                while (cursor.isAfterLast() == false) {
                    int id = cursor.getInt(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.ID_COLUMN
                        )
                    );

                    String employeeId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.EMPLOYEE_ID_COLUMN
                        )
                    );

                    String employeeCompanyId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                        )
                    );

                    String timeActionId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.TIME_ACTION_ID_COLUMN
                        )
                    );

                    String createdAt = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.CREATED_AT_COLUMN
                        )
                    );

                    JSONObject row = new JSONObject();
                    row.put(Values.Persistence.Database.ID_COLUMN, id);
                    row.put(Values.Persistence.Database.EMPLOYEE_ID_COLUMN, employeeId);
                    row.put(Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN, employeeCompanyId);
                    row.put(Values.Persistence.Database.TIME_ACTION_ID_COLUMN, timeActionId);
                    row.put(Values.Persistence.Database.CREATED_AT_COLUMN, createdAt);
                    ja.put(row);
                    cursor.moveToNext();
                }
                result.put("time_records", ja);
            }
        } catch (JSONException e) {
            e.printStackTrace(System.out);
        }

        cursor.close();
        db.close();
        return result;
    }

    public JSONObject getAllTimeRecords() {
        db = openDB();
        Cursor cursor = db.query(
            Values.Persistence.Database.TABLE_NAME_TIME_RECORDS,
            new String[] {
                Values.Persistence.Database.ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_ID_COLUMN,
                Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN,
                Values.Persistence.Database.TIME_ACTION_ID_COLUMN,
                Values.Persistence.Database.CREATED_AT_COLUMN
            },
            null, null, null, null, null, null
        );

        JSONObject result = new JSONObject();

        try {
            if (cursor.moveToFirst()) {
                JSONArray ja = new JSONArray();

                while (cursor.isAfterLast() == false) {
                    int id = cursor.getInt(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.ID_COLUMN
                        )
                    );

                    String employeeId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.EMPLOYEE_ID_COLUMN
                        )
                    );

                    String employeeCompanyId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN
                        )
                    );

                    String timeActionId = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.TIME_ACTION_ID_COLUMN
                        )
                    );

                    String createdAt = cursor.getString(
                        cursor.getColumnIndex(
                            Values.Persistence.Database.CREATED_AT_COLUMN
                        )
                    );

                    JSONObject row = new JSONObject();
                    row.put(Values.Persistence.Database.ID_COLUMN, id);
                    row.put(Values.Persistence.Database.EMPLOYEE_ID_COLUMN, employeeId);
                    row.put(Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN, employeeCompanyId);
                    row.put(Values.Persistence.Database.TIME_ACTION_ID_COLUMN, timeActionId);
                    row.put(Values.Persistence.Database.CREATED_AT_COLUMN, createdAt);
                    ja.put(row);
                    cursor.moveToNext();
                }
                result.put("time_records", ja);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        cursor.close();
        db.close();
        return result;
    }

    public void deleteAllTimeRecordsByCompanyId(String companyId) {
        db = openDB();
        db.delete(
            Values.Persistence.Database.TABLE_NAME_TIME_RECORDS,
            Values.Persistence.Database.EMPLOYEE_COMPANY_ID_COLUMN + "=?",
            new String[] { companyId }
        );
        db.close();
    }

    public void deleteTimeRecordById(int id) {
        db = openDB();
        db.delete(
            Values.Persistence.Database.TABLE_NAME_TIME_RECORDS,
            Values.Persistence.Database.ID_COLUMN + "=?",
            new String[] { String.valueOf(id) }
        );
        db.close();
    }
}
