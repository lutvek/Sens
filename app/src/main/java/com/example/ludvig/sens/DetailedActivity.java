package com.example.ludvig.sens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DetailedActivity extends AppCompatActivity {
    private static final String TAG = "Sens";
    private static final int DUMMY_SAMPLE_SIZE = 20;

    private long cur_sensor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();

        // Should handle the -1 case...
        cur_sensor_id = intent.getLongExtra(MainActivity.EXTRA_ID, -1);

        SensorDBItem sensor = MainActivity.getSensorByID(cur_sensor_id, MainActivity.db);

        getSupportActionBar().setTitle(sensor.name);

        setupViews(sensor);
        setupChart();
        setupFonts();
    }

    private void setupViews(SensorDBItem sensor) {
        ((TextView) findViewById(R.id.TVname)).setText(sensor.name);
        String formated_temp = String.format("°%.1f", sensor.temperature);
        ((TextView) findViewById(R.id.TVtemp)).setText(formated_temp);
    }


    private void setupChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entryArrayList = new ArrayList<>();
        ArrayList<String> datesArrayList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -DUMMY_SAMPLE_SIZE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-LL");
        Random random = new Random();

        int temp;
        String dateString;

        for (int i = 0; i < DUMMY_SAMPLE_SIZE; i++) {
            temp = random.nextInt(3) + 20;
            calendar.add(Calendar.DATE, 1);
            dateString = simpleDateFormat.format(calendar.getTime());
            Entry entry = new Entry(temp, i);
            entryArrayList.add(entry);
            datesArrayList.add(dateString);
        }

        LineDataSet dataSet = new LineDataSet(entryArrayList, "Temp");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSet.setDrawValues(false);

        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        dataSet.setColor(colorPrimary);
        dataSet.setCircleColor(colorPrimary);
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(datesArrayList, dataSet);

        chart.setData(lineData);

        // Chart settings

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        //chart.setTouchEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);

        chart.setDescription("");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(face);
        xAxis.setTextSize(12f);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setTypeface(face);
        axisLeft.setTextSize(12f);

        LimitLine lowerLimit = new LimitLine(23);
        lowerLimit.setLineColor(Color.RED);
        LimitLine upperLimit = new LimitLine(19);

        upperLimit.setLineColor(Color.RED);

        axisLeft.addLimitLine(lowerLimit);
        axisLeft.addLimitLine(upperLimit);

        // This barely works...
        chart.zoom(1f, 3f, 30, 30);
        chart.moveViewToY(20, YAxis.AxisDependency.LEFT);

        // Redraw the chart
        chart.invalidate();
    }

    private void setupFonts() {
        // add fonts for sensors
        TextView tv;
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");
        tv = (TextView) findViewById(R.id.TVname);
        tv.setTypeface(faceLight);
        tv = (TextView) findViewById(R.id.TVtemp);
        tv.setTypeface(faceLight);

        Typeface faceRegular = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Regular.ttf");

        tv = (TextView) findViewById(R.id.edit_button);
        tv.setTypeface(faceRegular);
    }

    public void editSensor(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(MainActivity.EXTRA_ID, cur_sensor_id);
        startActivity(intent);
    }
}
