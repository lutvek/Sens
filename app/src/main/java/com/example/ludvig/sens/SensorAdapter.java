package com.example.ludvig.sens;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<SensorDBItem> {

    Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
            "fonts/Raleway-Light.ttf");

    private static class ViewHolder {
        TextView sensorName;
        TextView sensorTemp;
        Switch notifications;
        ImageView connectionStatus;
    }

    public SensorAdapter(Context context, List<SensorDBItem> sensors) {
        super(context, R.layout.sensor_row, sensors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SensorDBItem sensor = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sensor_row, parent, false);

            viewHolder.sensorName = (TextView) convertView.findViewById(R.id.sensor_name);
            viewHolder.sensorTemp = (TextView) convertView.findViewById(R.id.sensor_temp);
            viewHolder.notifications = (Switch) convertView.findViewById(R.id.notifications);
            viewHolder.connectionStatus = (ImageView) convertView.findViewById(R.id.connected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.sensorName.setText(sensor.name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            viewHolder.notifications.setChecked(sensor.pushNotifications);
        }

        switch (sensor.connectionStatus){
            case SensorDBItem.UNINITIALIZED:
                viewHolder.sensorTemp.setText(R.string.connection_status_uninitialized);
                viewHolder.connectionStatus.setImageResource(R.drawable.circle_yellow);
                break;
            case SensorDBItem.DISCONNNECTED:
                viewHolder.sensorTemp.setText(R.string.connection_status_disconnected);
                viewHolder.connectionStatus.setImageResource(R.drawable.circle_red);
                break;
            case SensorDBItem.CONNECTED:
                String formated_temp = String.format("°%.1f", sensor.temperature);
                viewHolder.sensorTemp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                viewHolder.sensorTemp.setText(formated_temp);
                viewHolder.connectionStatus.setImageResource(R.drawable.circle_green);
                break;
        }

        viewHolder.sensorName.setTypeface(faceLight);
        viewHolder.sensorTemp.setTypeface(faceLight);

        return convertView;
    }
}
