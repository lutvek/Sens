package com.example.ludvig.sens;

/**
 * Object for each entry in database
 * se https://guides.codepath.com/android/Easier-SQL-with-Cupboard
 * for description of structure
 *
 * Created by Ludvig on 24/02/2016.
 */
public class SensorDBItem {

    public Long _id; // for cupboard
    public String name; // name of sensor
    public double maxTemp; // limits for warnings
    public double minTemp;
    public double temperature; // current temperature of sensor
    public boolean pushNotifications;
    // TODO add array with temperatures over 24 hours

    // create sensor to add in db
    public SensorDBItem(String name, double maxTemp, double minTemp, boolean pushNotifications) {
        this.name = name;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pushNotifications = pushNotifications;
    }

    // zero argument constructor (required)
    public SensorDBItem(){
        this.name ="noName";
        this.minTemp = 0;
        this.maxTemp = 0;
        this.temperature = 20;
        this.pushNotifications = false;
    }
}
