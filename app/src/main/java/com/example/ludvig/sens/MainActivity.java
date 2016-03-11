package com.example.ludvig.sens;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String EXTRA_ID = "com.example.ludvig.sens.EXTRA_ID";
    public final static int UPDATE_FREQUENCY = 60000; // 1 minute

    public static SQLiteDatabase db;
    private static final String TAG = "Sens";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // we have our own logo, so we hide the title
        getSupportActionBar().setTitle(null);


        /* adding menu */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(new SensorListClickListener());
        listView.setOnItemLongClickListener(new SensorListLongClickListener());

        // instantiate database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        displaySensors(db);

        addFonts();

        registerUpdateAlarm();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateSensorService.SENSOR_UPDATE);
        registerReceiver(receiver, filter);
        displaySensors(db);
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            displaySensors(db);
            Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
        }
    };

    /***************************** LAYOUT OPTIONS ************************/

    // add fonts to elements in frame
    private void addFonts() {
        // add fonts for sensors
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");

        TextView tv = (TextView) findViewById(R.id.sensor_small);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-MediumItalic.ttf"));

        Typeface faceRegular = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Regular.ttf");

        // add font for button
        Button button = (Button) findViewById(R.id.button);
        button.setTypeface(faceRegular);

    }

    /**************************** Clickable functions *****************************/
    // go to add new sensor activity
    public void addSensor(View view) {
        Intent intent = new Intent(this, AddSensor.class);
        startActivity(intent);
    }

    public void notificationSwitch(View view) {

    }

    /**************************** Database Options *****************************/

    public static long addSensorToDB(SensorDBItem sensor, SQLiteDatabase db) {
        return cupboard().withDatabase(db).put(sensor);
    }

    public static void updateSensor(SensorDBItem sensor, SQLiteDatabase db) {
        cupboard().withDatabase(db).put(sensor);
    }

    public static void deleteSensorFromDB(SensorDBItem sensor, SQLiteDatabase db) {
        cupboard().withDatabase(db).delete(sensor);
    }

    public static SensorDBItem getSensorByID(long id, SQLiteDatabase db) {
        return cupboard().withDatabase(db).get(SensorDBItem.class, id);
    }

    public static Iterator<SensorDBItem> getSensors(SQLiteDatabase db) {
        return cupboard().withDatabase(db).query(SensorDBItem.class).query().iterator();
    }

    private void clearDB(SQLiteDatabase db) {
        Cursor sensors = cupboard().withDatabase(db).query(SensorDBItem.class).getCursor();
        QueryResultIterable<SensorDBItem> itr = null;
        try {
            // Iterate Sensors
            itr = cupboard().withCursor(sensors).iterate(SensorDBItem.class);
            for (SensorDBItem sensor : itr) {
                deleteSensorFromDB(sensor, db); // delete sensor
            }
        } finally {
            // close the cursor
            if (itr != null) {
                itr.close();
            }
        }
    }

    private void displaySensors (SQLiteDatabase db) {
        final ListView listView = (ListView) findViewById(R.id.listview);

        ArrayList<SensorDBItem> list = new ArrayList<>();

        Cursor sensors = cupboard().withDatabase(db).query(SensorDBItem.class).getCursor();
        QueryResultIterable<SensorDBItem> itr = null;
        try {
            // Iterate Sensors
            itr = cupboard().withCursor(sensors).iterate(SensorDBItem.class);
            for (SensorDBItem sensor : itr) {
                list.add(sensor);
            }
        } finally {
            // close the cursor
            itr.close();
        }

        // Create an adaptor from the list using our SensorAdapter
        SensorAdapter adapter = new SensorAdapter(this, list);

        // Attach the adaptor to the listView
        listView.setAdapter(adapter);
    }

    /**************************** MENU OPTIONS *********************************/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void registerUpdateAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, UPDATE_FREQUENCY, pendingIntent);
    }

    /**************************** CLASSES *****************************/

    private class SensorListClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SensorDBItem sensor = (SensorDBItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), DetailedActivity.class);

            intent.putExtra(EXTRA_ID, sensor._id);
            startActivity(intent);
        }
    }

    private class SensorListLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            SensorDBItem sensor = (SensorDBItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);

            intent.putExtra(EXTRA_ID, sensor._id);
            startActivity(intent);
            // Should I return true here?
            return true;
        }
    }
}
