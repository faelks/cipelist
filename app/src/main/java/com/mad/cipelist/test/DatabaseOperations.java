package com.mad.cipelist.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mad.cipelist.test.Table.TableInfo;

/**
 * Created by Felix on 21/09/16.
 *
 * Test helper for an SQLite database
 */
public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableInfo.TABLE_NAME + "(" + TableInfo.USER_NAME + " TEXT," + TableInfo.USER_PASS + " TEXT );";

    /**
     * Creates a database ?
     * @param context
     */
    public DatabaseOperations(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DatabaseOperations", "Successfully created database");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
        Log.d("DatabaseOperations", "Successfully created table");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperations dop, String name, String password) {
        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.USER_NAME, name);
        cv.put(TableInfo.USER_PASS, password);
        long k = sq.insert(TableInfo.TABLE_NAME, null,cv);
        Log.d("DatabaseOperations", "One row inserted with ID" + k + " name: " + name);
    }

    public Cursor getInformation(DatabaseOperations dop) {
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {TableInfo.USER_NAME, TableInfo.USER_PASS};
        Cursor cr = sq.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }

    public void deleteUser(DatabaseOperations dop, String username) {
        String selection = TableInfo.USER_NAME + " LIKE ?;";
        SQLiteDatabase sq = dop.getWritableDatabase();
        String [] args = {username};
        sq.delete(TableInfo.TABLE_NAME, selection, args);
    }

}
