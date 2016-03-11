package com.example.ludvig.sens;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

        EditText maxTemp = (EditText) findViewById(R.id.max_temp);
        maxTemp.setText(String.valueOf(cur_sensor.maxTemp));

        EditText minTemp = (EditText) findViewById(R.id.min_temp);
        minTemp.setText(String.valueOf(cur_sensor.minTemp));

        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(enterName);
        editTexts.add(enterID);
        editTexts.add(maxTemp);
        editTexts.add(minTemp);

        Button button = (Button) findViewById(R.id.accept_button);

        TextValidator textValidator = new TextValidator(editTexts, button);

        enterName.addTextChangedListener(textValidator);
        enterID.addTextChangedListener(textValidator);
        maxTemp.addTextChangedListener(textValidator);
        minTemp.addTextChangedListener(textValidator);

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
        tv = (TextView) findViewById(R.id.delete_button);
        tv.setTypeface(faceRegular);
    }

    public void acceptChanges(View view) {

        EditText sensor_name = (EditText) findViewById(R.id.newSensorName);
        EditText sensor_id = (EditText) findViewById(R.id.newSensorID);
        EditText sensor_max = (EditText) findViewById(R.id.max_temp);
        EditText sensor_min = (EditText) findViewById(R.id.min_temp);
        Switch sensor_notifications = (Switch) findViewById(R.id.allow_alarm);

        cur_sensor.name = sensor_name.getText().toString();
        cur_sensor.sensId = sensor_id.getText().toString();
        cur_sensor.maxTemp = Double.valueOf(sensor_max.getText().toString());
        cur_sensor.minTemp = Double.valueOf(sensor_min.getText().toString());
        cur_sensor.pushNotifications = sensor_notifications.isChecked();

        MainActivity.updateSensor(cur_sensor, db);
        db.close();

        CharSequence text = "Note: Will use development sensor id to fetch data";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        finish();
    }

    public void cancel(View view) {
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
