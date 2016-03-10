package com.example.ludvig.sens;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    public static SQLiteDatabase db;

    private SensorDBItem cur_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Create a database instance
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        Intent intent = getIntent();

        // Should handle the -1 case...
        long sensor_id = intent.getLongExtra(MainActivity.EXTRA_ID, -1);

        cur_sensor = MainActivity.getSensorByID(sensor_id, MainActivity.db);

        // Setup the spinner
        Spinner spinner = (Spinner) findViewById(R.id.sensor_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sensors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);

        EditText enterName = (EditText) findViewById(R.id.newSensorName);
        enterName.setText(cur_sensor.name);

        EditText enterID = (EditText) findViewById(R.id.newSensorID);
        enterID.setText(cur_sensor.sensId);

        EditText maxTempText = (EditText) findViewById(R.id.max_temp);
        maxTempText.setText(String.valueOf(cur_sensor.maxTemp));

        EditText minTempText = (EditText) findViewById(R.id.min_temp);
        minTempText.setText(String.valueOf(cur_sensor.minTemp));

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
        tv = (TextView) findViewById(R.id.allow_alarm_text);
        tv.setTypeface(faceLight);

        Typeface faceRegular = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Regular.ttf");

        tv = (TextView) findViewById(R.id.accept_button);
        tv.setTypeface(faceRegular);
        tv = (TextView) findViewById(R.id.cancel_button);
        tv.setTypeface(faceRegular);
        tv = (TextView) findViewById(R.id.delete_button);
        tv.setTypeface(faceRegular);
    }

    public void addNewSensor(View view) {
        CharSequence text = "Note: Will use development sensor id to fetch data";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        finish();
    }

    public void back(View view) {
        finish();
    }

    public void deleteSensor(View view) {
        MainActivity.deleteSensorFromDB(cur_sensor, db);
        CharSequence text = cur_sensor.name + " deleted!";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
