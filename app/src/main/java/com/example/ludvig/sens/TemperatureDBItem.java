package com.example.ludvig.sens;

import java.util.Date;

public class TemperatureDBItem {

    public Long _id; // for cupboard
    public long sensorID;
    public double temperature; // current temperature of sensor
    public Date date;

    public TemperatureDBItem(long sensorID, double temperature) {
        this.sensorID = sensorID;
        this.temperature = temperature;
        this.date = new Date();
    }

    // zero argument constructor (required)
    public TemperatureDBItem(){
        this.sensorID = 0;
        this.temperature = 20;
        this.date = null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(sensorID);
        sb.append(temperature);
        sb.append(date.toString());
        return sb.toString();
    }
}
