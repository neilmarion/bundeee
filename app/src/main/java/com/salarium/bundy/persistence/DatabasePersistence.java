package com.salarium.bundy.persistence;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import com.salarium.bundy.values.*;
import java.io.File;

/* current database version: 2 */

/** Class that implements database operations
 * @author Neil Marion dela Cruz
 */
public class DatabasePersistence extends SQLiteOpenHelper {
    private Context context;
    protected String tableCreateQuery;
    protected SQLiteDatabase db;

    public DatabasePersistence(Context context) {
        super(
            context,
            Values.Persistence.Database.DATABASE_NAME,
            null,
            Values.Persistence.Database.Changes.Version3.DATABASE_VERSION
        );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Values.Persistence.Database.Changes.Version3.TABLE_CREATE_QUERY_EMPLOYEES);
        db.execSQL(Values.Persistence.Database.Changes.Version3.TABLE_CREATE_QUERY_TIME_RECORDS);
        this.db = db;
    }

    @Override
    public void onUpgrade(
        SQLiteDatabase db,
        int oldVersion,
        int newVersion
    ) {
        if (oldVersion == Values.Persistence.Database.Changes.Version2.DATABASE_VERSION) {
            db.execSQL(Values.Persistence.Database.Changes.Version3.ALTER_EMPLOYEES_TABLE_ADD_EMPLOYEE_ID);
            db.execSQL(Values.Persistence.Database.Changes.Version3.ALTER_EMPLOYEES_TABLE_ADD_FULL_NAME);
            db.execSQL(Values.Persistence.Database.Changes.Version3.ALTER_EMPLOYEES_TABLE_ADD_COMPANY_ID);
            db.execSQL(Values.Persistence.Database.Changes.Version3.TABLE_CREATE_QUERY_TIME_RECORDS);
            db.execSQL(Values.Persistence.Database.Changes.Version3.UPDATE_EMPLOYEES_TABLE_COPY_EMPLOYEE_ID);
        }
    }

    protected SQLiteDatabase getDB() {
        return getWritableDatabase();
    }

    protected String getDBPath() {
        SQLiteDatabase db = getDB();
        String path = db.getPath();
        db.close();
        return path;
    }

    protected SQLiteDatabase openDB() {
        return SQLiteDatabase.openOrCreateDatabase(getDBPath(), null);
    }
}
