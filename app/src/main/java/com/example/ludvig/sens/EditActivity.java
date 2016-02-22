package com.example.ludvig.sens;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Spinner spinner = (Spinner) findViewById(R.id.sensor_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sensors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // FIXME Temp values of fields
        EditText enterName= (EditText) findViewById(R.id.newSensorName);
        enterName.setText("Spis");

        EditText enterID= (EditText) findViewById(R.id.newSensorID);
        enterID.setText("2d0028000e47343432313031");

        EditText maxTempText = (EditText) findViewById(R.id.max_temp);
        maxTempText.setText("23");

        EditText minTempText = (EditText) findViewById(R.id.min_temp);
        minTempText.setText("19");

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
        finish();
    }

    public void back(View view) {
        finish();
    }
}
