package com.jcd.reciclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jcd.reciclerview.bd.EmCartagoDb;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {

    EmCartagoDb emDb;

    EditText edName;
    EditText edAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        emDb = new EmCartagoDb(this);

        edName = (EditText) findViewById(R.id.edName);
        edAdd = (EditText) findViewById(R.id.edAdd);
    }

    public void save(View view) {

        try {
            emDb.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        emDb.insertUsers(edName.getText().toString(), edAdd.getText().toString());
        emDb.close();

        edAdd.setText("");
        edName.setText("");

        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
    }

    public void list(View view) {
        try {
            emDb.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result =emDb.listUser();
        emDb.close();

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
