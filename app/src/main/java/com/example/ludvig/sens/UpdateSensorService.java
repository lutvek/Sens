package com.example.ludvig.sens;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class UpdateSensorService extends IntentService {
    public UpdateSensorService() {
        super("UpdateSensorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Iterator<SensorDBItem> sensorsIterator = MainActivity.getSensors(MainActivity.db);
        SensorDBItem sensor;
        InputStream is;

        while (sensorsIterator.hasNext()) {
            sensor = sensorsIterator.next();

            try {
                URL url = new URL("https://api.particle.io/v1/devices/2d0028000e47343432313031/temp?access_token=172b708ddc95af637876a01a36a6fc37019f8387");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(10000); /* MILLI */
                connection.setConnectTimeout(15000); /* MILLI */
                connection.setRequestMethod("GET");
                connection.setDoInput(true); /* Allows input */
                connection.connect();
                int response = connection.getResponseCode();
                Log.i("HTTP GET", "The response is: " + response + " for sensor " + sensor.name);
                is = connection.getInputStream();

                String contentAsString = convertStreamToString(is);
                JSONObject jsonObject = new JSONObject(contentAsString);
                double temp = Double.valueOf(jsonObject.getString("result"));

                Log.i("HTTP GET", String.valueOf(temp));
                connection.disconnect();

                sensor.temperature = temp;

                MainActivity.updateSensorTemp(sensor, MainActivity.db);

                if(is != null) is.close();
            } catch (Exception ignored) {}
        }
        
    }

    // Reads an InputStream and converts it to a String.
    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
