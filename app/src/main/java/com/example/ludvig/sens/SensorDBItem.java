package com.example.ludvig.sens;

/**
 * Object for each entry in database
 * se https://guides.codepath.com/android/Easier-SQL-with-Cupboard
 * for description of structure
 *
 * Created by Ludvig on 24/02/2016.
 */
public class SensorDBItem {

    public static final int CONNECTED = 1;
    public static final int DISCONNNECTED = -1;
    public static final int UNINITIALIZED = 0;

    public Long _id; // for cupboard
    public String name; // name of sensor
    public String sensId; // Id of sensor
    public double maxTemp; // limits for warnings
    public double minTemp;
    public double temperature; // current temperature of sensor
    public boolean pushNotifications;
    public int connectionStatus;

    // create sensor to add in db
    public SensorDBItem(String name, String sensId, double maxTemp, double minTemp, boolean pushNotifications) {
        this.name = name;
        this.sensId = sensId;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pushNotifications = pushNotifications;
        this.connectionStatus = UNINITIALIZED;
    }

    // zero argument constructor (required)
    public SensorDBItem(){
        this.name ="noName";
        this.sensId = null;
        this.minTemp = 0;
        this.maxTemp = 0;
        this.temperature = 20;
        this.pushNotifications = false;
        this.connectionStatus = UNINITIALIZED;
    }
}
