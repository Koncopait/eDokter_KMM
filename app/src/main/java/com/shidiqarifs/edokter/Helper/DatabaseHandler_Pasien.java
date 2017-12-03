package com.shidiqarifs.edokter.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler_Pasien extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Pasien.db";

    // User table name
    private static final String TABLE_PASIEN = "List_Pasien";

    // User Table Columns names
    private static final String COLUMN_PASIEN_ID = "id_pasien";
    private static final String COLUMN_PASIEN_NAME = "nama_pasien";
    private static final String COLUMN_PASIEN_KELUHAN = "keluhan";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_PASIEN + "("
            + COLUMN_PASIEN_ID + " TEXT," + COLUMN_PASIEN_NAME + " TEXT,"+ COLUMN_PASIEN_KELUHAN + " TEXT" + ")";
    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_PASIEN;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHandler_Pasien(Context context) {
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
     * @param pasien
     */
    public void addUser(Pasien pasien) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASIEN_ID, pasien.getId_pasien());
        values.put(COLUMN_PASIEN_NAME, pasien.getNama_pasien());
        values.put(COLUMN_PASIEN_KELUHAN, pasien.getKeluhan());

        // Inserting Row
        db.insert(TABLE_PASIEN, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public ArrayList<Pasien> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_PASIEN_ID,
                COLUMN_PASIEN_NAME,
                COLUMN_PASIEN_KELUHAN
        };
        // sorting orders
        String sortOrder =
                COLUMN_PASIEN_NAME + " ASC";
        ArrayList<Pasien> userList = new ArrayList<Pasien>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_PASIEN, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pasien pasien = new Pasien();
                pasien.setId_pasien(cursor.getString(cursor.getColumnIndex(COLUMN_PASIEN_ID)));
                pasien.setNama_pasien(cursor.getString(cursor.getColumnIndex(COLUMN_PASIEN_NAME)));
                pasien.setKeluhan(cursor.getString(cursor.getColumnIndex(COLUMN_PASIEN_KELUHAN)));
                // Adding user record to list
                userList.add(pasien);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_PASIEN,null,null);
        db.close();
    }

}