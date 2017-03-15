package com.jcd.reciclerview.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.jcd.reciclerview.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Argosoft03 on 28/02/2017.
 */

public class EmCartagoDb {


    public static final String T_USER = "Users";

    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "nameUser";
    public static final String KEY_USER_CEDU = "Cedula";
    public static final String KEY_USER_TELE = "Telefono";
    public static final String KEY_USER_ADD = "addressUser";
    public static final String KEY_USER_PHOTO = "Photo";



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
            super(context, "EmCartago", null, 4);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + T_USER + "( " + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_USER_NAME + " TEXT, " + KEY_USER_ADD + " TEXT, " + KEY_USER_PHOTO +  " TEXT, " + KEY_USER_CEDU + " TEXT, " + KEY_USER_TELE + " TEXT);");
            db.execSQL("CREATE TABLE " + T_HOUSES + "( " + KEY_HOUSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  + KEY_HOUSE_USER_ID + " INTEGER, " + KEY_HOUSE_Name + " TEXT, " + KEY_HOUSE_ADD + " TEXT, FOREIGN KEY (" + KEY_HOUSE_USER_ID + ") REFERENCES " + T_USER+"("+KEY_USER_ID+")););");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + T_USER);
            db.execSQL("DROP TABLE IF EXISTS " + T_HOUSES);
            onCreate(db );
        }
    }

    public void open() throws SQLException, IOException {
        myHelper = new MyHelper(context);
        nDB = myHelper.getWritableDatabase();

        if(!tieneValores()){
            insertValuesFromCSVFile();
        }
    }

    private void insertValuesFromCSVFile() throws IOException {

        //Open your local db as the input stream
        InputStreamReader myInput = new InputStreamReader(context.getAssets().open("bd_csv.csv"));

        BufferedReader buffer = new BufferedReader(myInput);
        //transfer bytes from the inputfile to the outputfile
        String line = "";
        int pos = 0;
        while ((line = buffer.readLine())!= null){

            if(pos != 0) {
                String[] colums = line.split(";");

                ContentValues cv = new ContentValues(3);
                cv.put(KEY_USER_ID, colums[0].trim());
                cv.put(KEY_USER_CEDU, colums[1].trim());
                cv.put(KEY_USER_NAME, colums[2].trim());
                cv.put(KEY_USER_TELE, colums[3].trim());
                cv.put(KEY_USER_ADD, colums[4].trim());
                nDB.insert(T_USER, null, cv);
            }
            pos++;
        }

        //Close the streams
        myInput.close();

    }

    public void exportToCSVFile() throws IOException {

        File ruta_sd = Environment.getExternalStorageDirectory();

        File f = new File(ruta_sd.getAbsolutePath(), "emcartago.csv");

        OutputStreamWriter fout =
                new OutputStreamWriter(
                        new FileOutputStream(f));

        Cursor cursor = nDB.rawQuery("select * from " + T_USER ,null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            String value = cursor.getInt(0) + ";" + cursor.getString(1) + ";" + cursor.getString(2) + ";" + cursor.getString(3) + ";" + cursor.getString(4) + "\n";
            fout.write(value);


        }
        fout.close();

    }

    private boolean tieneValores() {

        Cursor c = nDB.rawQuery("SELECT * FROM " +T_USER, null);

        if(c.moveToFirst()){
            return true;
        }

        return false;
    }

    public void close(){
        nDB.close();
    }

    public void insertUsers(String...values){

        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, values[0]);
        cv.put(KEY_USER_ADD, values[1]);
        cv.put(KEY_USER_CEDU, values[2]);
        cv.put(KEY_USER_TELE, values[3]);

        nDB.insert(T_USER, null, cv);
    }

    public void insertUsersPhoto(String...values){

        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, values[0]);
        cv.put(KEY_USER_ADD, values[1]);
        cv.put(KEY_USER_PHOTO, values[2]);

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

    public String listUserPhoto(){
        String resutl ="";

        Cursor c = nDB.rawQuery("select * from " + T_USER  + " where " + KEY_USER_PHOTO + " is not null", null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            resutl += "id: " + c.getInt(0) + "Name: " + c.getString(1) + " Add: " + c.getString(1) + " photo: " + c.getString(2) + "\n";

        }

        return resutl;
    }

    public String getPhoto(String id){
        String resutl ="";

        Cursor c = nDB.rawQuery("select " + KEY_USER_PHOTO + " from " + T_USER  + " where " + KEY_USER_ID + " = " + id, null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            resutl = c.getString(0) ;

        }

        return resutl;
    }

}
