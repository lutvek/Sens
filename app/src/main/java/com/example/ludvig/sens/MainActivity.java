package com.example.ludvig.sens;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SQLiteDatabase db; //database accessible from all activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* adding menu */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // instantiate database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // display sensors in content_main.xml

        displaySensors(db);

        // add fonts to page
        addFonts();
        
        registerUpdateAlarm();
    }

    public void onResume() {
        displaySensors(db);
        super.onResume();
    }

    /***************************** LAYOUT OPTIONS ************************/

    // add fonts to elements in frame
    private void addFonts() {
        // add fonts for sensors
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/Raleway-Light.ttf");

        TextView tv = (TextView) findViewById(R.id.sensor_small);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-MediumItalic.ttf"));
        tv = (TextView) findViewById(R.id.online_small);
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

    // go to Detailed
    public void detailedView(View view) {
        Intent intent = new Intent(this, Detailed.class);
        startActivity(intent);
    }

    public void notificationSwitch(View view) {

    }

    /**************************** Database Options *****************************/

    // add sensor to db
    public static long addSensorToDB(SensorDBItem sensor, SQLiteDatabase db) {
        return cupboard().withDatabase(db).put(sensor);
    }

    // delete sensor from db
    public static void deleteSensorFromDB(SensorDBItem sensor, SQLiteDatabase db) {
        cupboard().withDatabase(db).delete(sensor);
    }

    // delete all sensors from db
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
            itr.close();
        }
    }

    // Display sensors in db to content_main.xml
    private void displaySensors (SQLiteDatabase db) {

        // Find the listview in the layout
        final ListView listView = (ListView) findViewById(R.id.listview);

        // Create and populate a list of Sensors
        ArrayList<SensorDBItem> list = new ArrayList<>();

        // output sensors in database to main_content
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

    @Override
    // TODO Removing this removes the three dots menu, but also makes the logo not centered.
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
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
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 60000, pendingIntent);
    }

    /**************************** CLASSES *****************************/
    // sensor adapter for displaying sensors in main_content
    private class SensorAdapter extends ArrayAdapter<SensorDBItem> {

        public SensorAdapter(Context context, List<SensorDBItem> objects) {
            super(context, 0, objects);
        }

        // Bad implementation
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SensorDBItem sensor = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensor_row, parent, false);
            }

            TextView sensor_name = (TextView) convertView.findViewById(R.id.sensor_name);
            TextView sensor_temp = (TextView) convertView.findViewById(R.id.sensor_temp);
            Switch notifications = (Switch) convertView.findViewById(R.id.notifications);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                notifications.setChecked(sensor.pushNotifications);
            }
            // set text
            sensor_name.setText(sensor.name);
            sensor_temp.setText(String.valueOf(sensor.temperature));
            // set fonts
            Typeface faceLight = Typeface.createFromAsset(getAssets(),
                    "fonts/Raleway-Light.ttf");
            sensor_name.setTypeface(faceLight);
            sensor_temp.setTypeface(faceLight);

            return convertView;
        }
    }

}
