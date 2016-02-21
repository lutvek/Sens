package com.example.ludvig.sens;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

public class Detailed extends AppCompatActivity {

    private static final String TAG = "Sens";
    private static final int SAMPLE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        setupChart();

        setupFonts();

    }

    private void setupChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entryArrayList = new ArrayList<>();
        ArrayList<String> datesArrayList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -SAMPLE_SIZE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-LL");
        Random random = new Random();

        int temp;
        String dateString;

        for (int i = 0; i < SAMPLE_SIZE; i++) {
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
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");
        tv = (TextView) findViewById(R.id.TVname);
        tv.setTypeface(face);
        tv = (TextView) findViewById(R.id.TVtemp);
        tv.setTypeface(face);

    }
}
