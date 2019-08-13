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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView stepsSinceOpenedText;
    private TextView totalStepsTakenText;

    private Pedometer pedometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get layout elements
        stepsSinceOpenedText = findViewById(R.id.main_opened_steps_taken);
        totalStepsTakenText = findViewById(R.id.main_steps_taken);

        stepsSinceOpenedText.setText("0");

        //Create Pedometer object
        pedometer = new Pedometer(this);
        //Setup listener for Pedometer object
        pedometer.setPodometerListener(new Pedometer.PedometerListener(){
            @Override
            public void onSensorChanged(int stepsSinceOpened, int stepsTotal) {
                stepsSinceOpenedText.setText(String.valueOf(stepsSinceOpened));
                totalStepsTakenText.setText(String.valueOf(stepsTotal));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pedometer.registerSensorListener();
    }


}
