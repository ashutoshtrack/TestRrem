package com.example.ashutosh.testrreminderui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "Taskyermania.db";
    public static final String TABLE_NAME = "Task_tablerman";

    public static final String COL_1 = "ID";

    public static final String COL_2 = "NAME";

    public static final String COL_3 = "AMOUNT";

    public static final String COL_4 = "PHOTOS";
    public static final String COL_5 = "DATE";
    public static final String COL_6 = "DUEDATE";
    public static final String COL_7 = "NOTES";
    public static final String COL_8 = "URL";




    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT,AMOUNT INTEGER,PHOTOS BLOG,DATE TEXT," +
                "DUEDATE TEXT,NOTES TEXT," +
                "URL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);

    }

    public boolean insertData(String name,String amount,byte[] photos,String dater,String duedate,String notes,String url ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, name);
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, photos);
        contentValues.put(COL_5, dater);
        contentValues.put(COL_6, duedate);
        contentValues.put(COL_7, notes);
        contentValues.put(COL_8, url);


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

public byte[] getPhoto(int id){
    byte[] num = new byte[0];
    SQLiteDatabase db = this.getWritableDatabase();



    String query = "select * from " +TABLE_NAME+ " where "+COL_1+ " = "+id ;
    Cursor resi = db.rawQuery(query,null);

    if(resi != null && resi.moveToFirst()){
        num = resi.getBlob(3);
        resi.close();
    }

    // int column = resi.getColumnIndex(COL_4);
 // byte[] photo=resi.getBlob(column);

    Log.i("getPhotodata","Executed Safely");
    return num;
}


public int getIdkarantest(){
int nam = 0;
    SQLiteDatabase db = this.getWritableDatabase();


    String query = "select * from " +TABLE_NAME;
    Cursor resi = db.rawQuery(query,null);
    if(resi != null && resi.moveToLast()){

nam = Integer.parseInt(resi.getString(0));
        resi.close();
    }
return   nam;
}
    public String getNaamkarantest(int id){
        String nam = new String();
        SQLiteDatabase db = this.getWritableDatabase();


        String query = "select * from " +TABLE_NAME+ " where "+COL_1+ " = "+id ;
        Cursor resi = db.rawQuery(query,null);
        if(resi != null && resi.moveToLast()){

            nam = resi.getString(1);
            resi.close();
        }
        return   nam;
    }


    public String getAmountkarantest(){
        String nam = new String();
        SQLiteDatabase db = this.getWritableDatabase();


        String query = "select * from " +TABLE_NAME;
        Cursor resi = db.rawQuery(query,null);
        if(resi != null && resi.moveToLast()){
            nam = resi.getString(2);
            resi.close();
        }
        return   nam;
    }

}