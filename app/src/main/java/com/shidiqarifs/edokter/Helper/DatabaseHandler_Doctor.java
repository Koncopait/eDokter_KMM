package com.shidiqarifs.edokter.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler_Doctor extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Dokter.db";

    // User table name
    private static final String TABLE_DOKTER = "List_Dokter";

    // User Table Columns names
    private static final String COLUMN_DOCTOR_ID = "id_dokter";
    private static final String COLUMN_DOCTOR_NAME = "nama_dokter";
    private static final String COLUMN_DOCTOR_SPESIALIS = "spesialis";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_DOKTER + "("
            + COLUMN_DOCTOR_ID + " TEXT," + COLUMN_DOCTOR_NAME + " TEXT,"
            + COLUMN_DOCTOR_SPESIALIS + " TEXT)";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_DOKTER;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHandler_Doctor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param dokter
     */
    public void addUser(Dokter dokter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_ID, dokter.getId_dokter());
        values.put(COLUMN_DOCTOR_NAME, dokter.getNama_dokter());
        values.put(COLUMN_DOCTOR_SPESIALIS, dokter.getSpesialis_dokter());

        // Inserting Row
        db.insert(TABLE_DOKTER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public ArrayList<Dokter> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_DOCTOR_ID,
                COLUMN_DOCTOR_NAME,
                COLUMN_DOCTOR_SPESIALIS,
        };
        // sorting orders
        String sortOrder =
                COLUMN_DOCTOR_NAME + " ASC";
        ArrayList<Dokter> userList = new ArrayList<Dokter>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_DOKTER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Dokter dokter = new Dokter();
                dokter.setId_dokter(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_ID)));
                dokter.setNama_dokter(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_NAME)));
                dokter.setSpesialis_dokter(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_SPESIALIS)));
                // Adding user record to list
                userList.add(dokter);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public void deleteUser() {
        String id="";
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_DOKTER,null,null);
        db.close();
    }

}