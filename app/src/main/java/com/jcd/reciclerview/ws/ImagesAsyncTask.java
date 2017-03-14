package com.jcd.reciclerview.ws;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Argosoft03 on 13/03/2017.
 */

public class ImagesAsyncTask extends AsyncTask<String, Void, Boolean> {

    Context context;

    public ImagesAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {

            HttpURLConnection urlConnection = null;

            URL url = new URL("http://192.168.1.110:55899/Api/Images/clienteImage/");

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
            cliente.put("stringFoto",params[0]);
            cliente.put("nameFoto",params[1]);



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
                return false;
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Toast.makeText(context, "Resultado: " + aBoolean.toString(), Toast.LENGTH_LONG).show();
    }
}
