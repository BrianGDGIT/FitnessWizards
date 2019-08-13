package com.example.fitnesswizards;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Pedometer implements SensorEventListener {
    private Context context;
    //Sensor
    private SensorManager sensorManager;
    private Sensor sensor;

    //Custom listener
    private PedometerListener listener;

    //Step counter variable
    private int stepsOffset = 0;
    int stepsSinceOpened;
    int stepsTotal;

    public Pedometer(Context context){
        this.context = context;
        //Instatiate step counter sensor
        sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Instatiate listener
        listener = null;
    }

    @Override
    public void onSensorChanged (SensorEvent e){
        if (stepsOffset == 0) {
            // initial value
            stepsOffset = (int)e.values[0];
        }

        //set steps since app opened
        stepsSinceOpened = (int)e.values[0] - stepsOffset;

        //set total steps
        stepsTotal = (int)e.values[0];

        //Trigger PodometerListener
        listener.onSensorChanged(stepsSinceOpened, stepsTotal);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void registerSensorListener(){
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    public interface PedometerListener{
        //When onSesnsorChange event occurs pass data up the chain to the main activity
        void onSensorChanged(int stepsSinceOpened, int stepsTotal);
    }

    public void setPodometerListener(PedometerListener listener){
        this.listener = listener;
    }
}
