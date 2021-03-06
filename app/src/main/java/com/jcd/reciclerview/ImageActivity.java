package com.jcd.reciclerview;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcd.reciclerview.bd.EmCartagoDb;
import com.jcd.reciclerview.ws.ImagesAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    EmCartagoDb nbd;
    ImageView imPhoto;
    File file;
    EditText edIdUser;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        nbd = new EmCartagoDb(this);

        imPhoto = (ImageView) findViewById(R.id.im_photo);
        edIdUser = (EditText) findViewById(R.id.edIdUser);

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
    }

    public void tomarFoto(View view) {

        try
        {

//Este código es previo al evento de activar la cámara de fotos.

            Intent icamara = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Time now = new Time();
            String archivo = "";
            now.setToNow();

//Aquí me creo un archivo (file) cuyo nombre será un código + un tipo + hora

            archivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/app_camera/"+ "ID_" + now.format2445().toString() +".jpg";
            file = new File(archivo);



//Finalmente ejecuto la actividad cámara pasándole dicho archivo como parámetro

            Uri outputFileUri = Uri.fromFile(file);
            icamara.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(icamara,0);

        }

        catch(Exception e)
        {
            Log.e("Errorr: " ,e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==0 && resultCode == RESULT_OK)
        {

/*Convierto el archivo generado en la previa a un bitmap para tratarlo como imagen y las últimas pruebas me han dado a entender que el fallo está en alguna de las siguientes 4 líneas*/

                Bitmap mybit = BitmapFactory.decodeFile(file.getAbsolutePath());
            String filename = file.getName();
                try {
//ruta en la Base de datos
                    nbd.open();
                    nbd.insertUsersPhoto("car", "12", file.getAbsolutePath());
                    nbd.close();

//Cambiamos el tamaño
                    mybit = Bitmap.createScaledBitmap(mybit, (int) (mybit.getWidth() * 0.8), (int) (mybit.getHeight() * 0.8), true);
//Guardamos el archivo nuevo
                    FileOutputStream ostream = new FileOutputStream(file);
                    mybit.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();

//Mostramos
                    imPhoto.setImageBitmap(mybit);
                    imPhoto.setScaleType(ImageView.ScaleType.FIT_XY);


                    /*PArte WS*/
                    //Deoodificamos la Imagen en Base64


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mybit.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] byteArrayImage = baos.toByteArray();


                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    //encodedImage = encodedImage.replace("\n", "%20");

                    ImagesAsyncTask asyncTask = new ImagesAsyncTask(this);
                    asyncTask.execute(encodedImage, filename);

                }

            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),"Se ha producido un error, vuelva a tomar la foto", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mostrarIdFoto(View view) {

        try {
            nbd.open();

            String result = nbd.listUserPhoto();
            nbd.close();

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPhoto(View view) {
        try {
            nbd.open();

            String photo = nbd.getPhoto(edIdUser.getText().toString());
            nbd.close();

            file = new File(photo);
            Bitmap mybit = BitmapFactory.decodeFile(file.getAbsolutePath());
            imPhoto.setImageBitmap(mybit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
