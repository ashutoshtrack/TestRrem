package com.example.ashutosh.testrreminderui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "Tasky.db";
    public static final String TABLE_NAME = "Task_table";

    public static final String COL_1 = "ID";

    public static final String COL_2 = "NAME";

    public static final String COL_3 = "AMOUNT";

    public static final String COL_4 = "PHOTOS";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,AMOUNT INTEGER,PHOTOS BLOG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);

    }

    public boolean insertData(String name,String amount,byte[] photos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, name);
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, photos);


        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getALLData() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " +TABLE_NAME;
        Cursor res = db.rawQuery(query,null);
        Log.i("getAlldata","Executed Safely");
        return res;
    }
    public void deleteEntry(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Deletes a row given its rowId, but I want to be able to pass
        // in the name of the KEY_NAME and have it delete that row.
        //db.delete(TABLE_NAME, COL_1 + "=" + id, null);
        db.delete(TABLE_NAME,COL_2 + "= '" +name+"'",null);

    }



}