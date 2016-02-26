package com.example.ludvig.sens;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class AddSensor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        Spinner spinner = (Spinner) findViewById(R.id.sensor_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sensors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // add fonts
        addFonts();
    }

    private void addFonts() {
        TextView tv = (TextView) findViewById(R.id.enter_name);
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.enter_id);
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.sensor_type);
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.max_temp_text);
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.min_temp_text);
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.allow_alarm);
        tv.setTypeface(faceLight);

        Typeface faceRegular = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Regular.ttf");

        tv = (TextView) findViewById(R.id.accept_button);
        tv.setTypeface(faceRegular);
        tv = (TextView) findViewById(R.id.cancel_button);
        tv.setTypeface(faceRegular);
    }

    // clicking cancel
    public void back(View view) {
        finish();
    }

    // clicking accept: adding new sensor to database
    public void addNewSensor(View view) {
        // TODO error handling
        TextView sensor_name = (TextView) findViewById(R.id.newSensorName);
        TextView sensor_id = (TextView) findViewById(R.id.newSensorID);
        TextView sensor_max = (TextView) findViewById(R.id.max_temp);
        TextView sensor_min = (TextView) findViewById(R.id.min_temp);
        Switch sensor_notifications = (Switch) findViewById(R.id.allow_alarm);

        String name = sensor_name.getText().toString();
        double max = Double.valueOf(sensor_max.getText().toString());
        double min = Double.valueOf(sensor_min.getText().toString());
        boolean notifications = sensor_notifications.isChecked();

        SensorDBItem newSensor = new SensorDBItem(name, max, min, notifications);
        MainActivity.addSensorToDB(newSensor, MainActivity.db);

        finish();
    }
}
