package com.example.ludvig.sens;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
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
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.enter_id);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.sensor_type);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.max_temp_text);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.min_temp_text);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.allow_alarm_text);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.accept_button);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.cancel_button);
        tv.setTypeface(face);
    }

    // clicking cancel
    public void back(View view) {
        finish();
    }
    // clicking accept: adding new sensor to database and updating
    public void addNewSensor(View view) {
        // TODO add new sensor to SQLite database and update
        finish();
    }
}
