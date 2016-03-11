package com.example.ludvig.sens;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddSensor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        Spinner spinner = (Spinner) findViewById(R.id.sensor_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sensors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);


        setupValidator();

        setFonts();
    }

    private void setupValidator(){
        final Button button = (Button) findViewById(R.id.accept_button);
        button.setEnabled(false);

        final EditText sensor_name = (EditText) findViewById(R.id.newSensorName);
        final EditText sensor_id = (EditText) findViewById(R.id.newSensorID);
        final EditText min_temp = (EditText) findViewById(R.id.min_temp);
        final EditText max_temp = (EditText) findViewById(R.id.max_temp);

        ArrayList<EditText> editTexts = new ArrayList<>();

        editTexts.add(sensor_name);
        editTexts.add(sensor_id);
        editTexts.add(min_temp);
        editTexts.add(max_temp);

        TextValidator validator = new TextValidator(editTexts, button);

        sensor_name.addTextChangedListener(validator);
        sensor_id.addTextChangedListener(validator);
        min_temp.addTextChangedListener(validator);
        max_temp.addTextChangedListener(validator);
    }

    private void setFonts() {
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
    public void cancel(View view) {
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
        String sensId = sensor_id.getText().toString();
        double max = Double.valueOf(sensor_max.getText().toString());
        double min = Double.valueOf(sensor_min.getText().toString());
        boolean notifications = sensor_notifications.isChecked();

        SensorDBItem newSensor = new SensorDBItem(name, sensId, max, min, notifications);
        MainActivity.addSensorToDB(newSensor, MainActivity.db);

        CharSequence text = "Note: Will use development sensor id to fetch data";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

        finish();
    }

}
