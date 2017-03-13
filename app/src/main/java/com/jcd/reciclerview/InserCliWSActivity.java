package com.jcd.reciclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class InserCliWSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inser_cli_ws);

        //TareaWSInsertar ts = new TareaWSInsertar();
        //ts.execute();

        TareaWSMostrar mostrar = new TareaWSMostrar();
        mostrar.execute();
    }


    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            try {

                HttpURLConnection urlConnection = null;

                URL url = new URL("http://192.168.1.110:55899/Api/Images/guardarClietne/");

                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject cliente   = new JSONObject();
                cliente.put("nombre","adm");
                cliente.put("cedula", "pwd");


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cliente.toString());
                wr.flush();


                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    System.out.println("" + sb.toString());
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }

                String jsonString = "";


                jsonString = sb.toString();

                if (!jsonString.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(InserCliWSActivity.this, "Insertado", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class TareaWSMostrar extends AsyncTask<String,Integer,Boolean> {


        String[] clientes = new String[]{};

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            try {

                HttpURLConnection urlConnection = null;

                URL url = new URL("http://192.168.1.110:55899/Api/Images/Clientes/");

                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestMethod("POST");


                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                urlConnection.setDoOutput(true);

                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                //char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                jsonString = sb.toString();

                JSONArray respJSON = new JSONArray(jsonString);

                clientes = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    int idCli = obj.getInt("id");
                    String nombCli = obj.getString("nombre");
                    String ceduCli = obj.getString("cedula");

                    clientes[i] = "" + idCli + "-" + nombCli + "-" + ceduCli;
                }


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(InserCliWSActivity.this, "Hay: " + clientes.length + " 1 : " + clientes[0], Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String encodeImageBase64(Bitmap bitmap) {

        Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage  = baos.toByteArray();

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap decodeImageBase64(String image){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

}
