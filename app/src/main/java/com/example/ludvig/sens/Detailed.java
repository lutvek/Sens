package com.example.ludvig.sens;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

public class Detailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entryArrayList = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        Random random = new Random();
        int temp;

        for (int i = 0; i < 10; i++) {
            temp = random.nextInt(30);
            Entry entry = new Entry(temp, i);
            entryArrayList.add(entry);
            xVals.add("Test");
        }

        LineDataSet dataSet = new LineDataSet(entryArrayList, "Temp");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData lineData = new LineData(xVals, dataSet);

        chart.setData(lineData);
        chart.invalidate();

        addFonts();

    }

    private void addFonts() {
        // add fonts for sensors
        TextView tv;
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");
        tv = (TextView) findViewById(R.id.TVname);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.TVtemp);
        tv.setTypeface(face);

    }

}
