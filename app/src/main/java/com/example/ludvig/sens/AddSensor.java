package com.example.ludvig.sens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddSensor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);
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
