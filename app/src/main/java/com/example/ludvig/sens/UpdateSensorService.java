package com.example.ludvig.sens;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UpdateSensorService extends IntentService {
    public UpdateSensorService() {
        super("UpdateSensorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InputStream is = null;
        try {
            URL url = new URL("https://api.particle.io/v1/devices/2d0028000e47343432313031/temp?access_token=172b708ddc95af637876a01a36a6fc37019f8387");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(10000); /* MILLI */
            connection.setConnectTimeout(15000); /* MILLI */
            connection.setRequestMethod("GET");
            connection.setDoInput(true); /* Allows input */
            connection.connect();
            int response = connection.getResponseCode();
            Log.d("HTTP GET", "The response is: " + response);
            is = connection.getInputStream();

            String contentAsString = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(contentAsString);
            String temp = jsonObject.getString("result");
            Log.d("HTTP GET", temp);
            if(connection != null) connection.disconnect();
            if(is != null) is.close();
        } catch (Exception e) {
        }
    }
    // Reads an InputStream and converts it to a String.
    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
