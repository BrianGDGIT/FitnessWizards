package com.example.fitnesswizards;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MainActivity extends AppCompatActivity {
    //Permissions constant
    private static final int MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView stepsSinceOpenedText;
    private TextView totalStepsTakenText;

    private Pedometer pedometer;

    private MapView mapView;
    private MapboxMap map;
    private Style mapStyle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mapbox - Get instancemust be done before inflating the View
        Mapbox.getInstance(this, "pk.eyJ1IjoiYnJpYW5jcyIsImEiOiJjanpidzFkdDQwMDllM21zYTR2cHhlOHM2In0.ugYZLxfaanhI5w3WeGJzwA");
        setContentView(R.layout.activity_main);

        //Mapbox
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                //Save reference to Map for later use
                map = mapboxMap;

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        //Save reference to map style
                        mapStyle = style;

                        enableLocationComponent();

                    }
                });
            }
        });

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

    private void enableLocationComponent(){
        //Check to see if permisions are enabled
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Get location component
            LocationComponent locationComponent = map.getLocationComponent();

            //Setup location component options
            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(this)
                    .build();

            //Setup location component activation options
            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(this, mapStyle)
                    .build();

            //Activate location component
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            //Enable to make visible
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }else{
            //Show dialog
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION);
            enableLocationComponent();
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pedometer.registerSensorListener();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
