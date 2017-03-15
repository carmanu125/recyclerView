package com.jcd.reciclerview;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jcd.reciclerview.bd.EmCartagoDb;

import java.io.IOException;

public class BDCSVActivity extends AppCompatActivity {
    
    EmCartagoDb ndb;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcsv);

        //Versiones 6 +
        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        
        ndb = new EmCartagoDb(this);

        try {
            ndb.open();
            String result = ndb.listUser();
            ndb.close();
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void export(View view) {
        try {
            ndb.open();
            ndb.exportToCSVFile();
            ndb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
