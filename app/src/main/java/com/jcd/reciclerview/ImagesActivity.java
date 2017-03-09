package com.jcd.reciclerview;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.util.UUID;

public class ImagesActivity extends AppCompatActivity implements View.OnClickListener {

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Declaring views
    private Button buttonElegir;
    private Button buttonSubir;
    private ImageView imageView;
    private EditText editText;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    private final String SERVER_URL = "192.168.1.133";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonElegir = (Button) findViewById(R.id.buttonChoose);
        buttonSubir = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonElegir.setOnClickListener(this);
        buttonSubir.setOnClickListener(this);

    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onClick(View v) {

    }
}
