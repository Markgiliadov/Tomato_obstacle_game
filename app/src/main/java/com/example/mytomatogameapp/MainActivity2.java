package com.example.mytomatogameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    Sensor mySensor;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.d("Main Activity: ","onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv = (TextView)findViewById(R.id.textView);

        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity2.this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("Main Activity2: ", "onCreate: Registered rotating listener");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        float x = sensorEvent.values[0];
        if (x >= -0.8) {
            tv.setText("LEFT");
//            DirectionAction action = DirectionAction.LEFT;
//            moveCarBySensors(action);
        } else if (x < 0.8) {
            tv.setText("RIGHT");
//            DirectionAction action = DirectionAction.RIGHT;
//            moveCarBySensors(action);
        }
//        tv.setText("X: "+Math.sin(sensorEvent.values[0])+"\nY: "+ Math.sin(sensorEvent.values[2])+"\nZ: "+ Math.sin(sensorEvent.values[1]));
//        Log.d("Main Activity: ", "onSensorChanged: X: " + sensorEvent.values[0] + "Y: " + sensorEvent.values[2]);
    }
}