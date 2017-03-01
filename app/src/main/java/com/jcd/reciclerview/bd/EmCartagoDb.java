package com.jcd.reciclerview.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Argosoft03 on 28/02/2017.
 */

public class EmCartagoDb {


    public static final String T_USER = "Users";

    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "nameUser";
    public static final String KEY_USER_ADD = "addressUser";


    public static final String T_HOUSES = "Houses";

    public static final String KEY_HOUSE_ID = "id";
    public static final String KEY_HOUSE_USER_ID = "idUser";
    public static final String KEY_HOUSE_Name = "name";
    public static final String KEY_HOUSE_ADD = "address";

    private MyHelper myHelper;
    private Context context;
    private SQLiteDatabase nDB;

    public EmCartagoDb(Context context) {
        this.context = context;

    }

    private class MyHelper extends SQLiteOpenHelper{


        public MyHelper(Context context) {
            super(context, "EmCartago", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + T_USER + "( " + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_USER_NAME + " TEXT, " + KEY_USER_ADD + " TEXT);");
            db.execSQL("CREATE TABLE " + T_HOUSES + "( " + KEY_HOUSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  + KEY_HOUSE_USER_ID + " INTEGER, " + KEY_HOUSE_Name + " TEXT, " + KEY_HOUSE_ADD + " TEXT, FOREIGN KEY (" + KEY_HOUSE_USER_ID + ") REFERENCES " + T_USER+"("+KEY_USER_ID+")););");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + T_USER);
            db.execSQL("DROP TABLE IF EXISTS " + T_HOUSES);
            onCreate(db );
        }
    }

    public void open() throws SQLException{
        myHelper = new MyHelper(context);
        nDB = myHelper.getWritableDatabase();
    }

    public void close(){
        nDB.close();
    }

    public void insertUsers(String...values){

        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, values[0]);
        cv.put(KEY_USER_ADD, values[1]);

        nDB.insert(T_USER, null, cv);
    }

    public String listUser(){
        String resutl ="";

        Cursor c = nDB.rawQuery("select * from " + T_USER, null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            resutl += "id: " + c.getInt(0) + "Name: " + c.getString(1) + " Add: " + c.getString(1) + "\n";

        }

        return resutl;
    }

}
