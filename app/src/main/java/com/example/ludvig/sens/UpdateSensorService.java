package com.example.ludvig.sens;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class UpdateSensorService extends IntentService {
    public UpdateSensorService() {
        super("UpdateSensorService");
    }

    public static SQLiteDatabase db;
    public static final String SENSOR_UPDATE = "com.example.ludvig.sens.SENSOR_UPDATE";

    @Override
    protected void onHandleIntent(Intent incoming_intent) {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        Iterator<SensorDBItem> sensorsIterator = MainActivity.getSensors(db);
        SensorDBItem sensor;
        InputStream is;
        boolean connected;

        while (sensorsIterator.hasNext()) {
            sensor = sensorsIterator.next();

            connected = sensorIsConnected("https://api.particle.io/v1/devices/2d0028000e47343432313031/?access_token=172b708ddc95af637876a01a36a6fc37019f8387");

            if (connected) {
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
                    sensor.connectionStatus = SensorDBItem.CONNECTED;

                    if (is != null) is.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                    sensor.connectionStatus = SensorDBItem.DISCONNECTED;
                } catch (Exception ignored) {}
            } else {
                sensor.connectionStatus = SensorDBItem.DISCONNECTED;
            }

            MainActivity.updateSensor(sensor, db);
        }

        Intent broadcast_intent = new Intent(SENSOR_UPDATE);
        sendBroadcast(broadcast_intent);

        db.close();
    }

    private boolean sensorIsConnected(String url_input) {
        boolean connectionStatus = false;

        try {
            URL url = new URL(url_input);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();

            String contentAsString = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(contentAsString);
            connectionStatus = jsonObject.getBoolean("connected");

            connection.disconnect();
        } catch (Exception ignored) {}

        return connectionStatus;
    }


    // Reads an InputStream and converts it to a String.
    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
