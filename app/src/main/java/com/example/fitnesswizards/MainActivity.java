package com.example.fitnesswizards;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView stepsTakenText;
    private TextView totalStepsTakenText;

    //Sensor
    private SensorManager sensorManager;
    private Sensor sensor;

    //Step counter variable
    private int steps = 0;
    private int stepsOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Instatiate step counter sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);



        //Get layout elements
        stepsTakenText = findViewById(R.id.main_opened_steps_taken);
        totalStepsTakenText = findViewById(R.id.main_steps_taken);

        //convert steps to string
        stepsTakenText.setText("0");

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged (SensorEvent e){

        Log.d(TAG, "onSensorChanged: ");
        if (stepsOffset == 0) {
            // initial value
            stepsOffset = (int)e.values[0];
        }

        //set steps since app opened
        stepsTakenText.setText(String.valueOf((int)e.values[0] - stepsOffset));

        //set total steps
        totalStepsTakenText.setText(String.valueOf((int)e.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
