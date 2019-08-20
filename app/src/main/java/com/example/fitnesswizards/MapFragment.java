package com.example.fitnesswizards;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    //Fragment View
    View view;
    //Permissions constant
    private static final int MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView stepsSinceOpenedText;
    private TextView totalStepsTakenText;

    private Pedometer pedometer;

    private MapView mapView;
    private MapboxMap map;
    private Style mapStyle;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Mapbox - Get instancemust be done before inflating the View
        Mapbox.getInstance(getActivity(), "pk.eyJ1IjoiYnJpYW5jcyIsImEiOiJjanpidzFkdDQwMDllM21zYTR2cHhlOHM2In0.ugYZLxfaanhI5w3WeGJzwA");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);


        //Mapbox
        mapView = view.findViewById(R.id.mapView);
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
        return view;
    }

    private void enableLocationComponent(){
        //Check to see if permisions are enabled
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Get location component
            LocationComponent locationComponent = map.getLocationComponent();

            //Setup location component options
            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(getActivity())
                    .foregroundDrawable(R.drawable.wizard)
                    .build();

            //Setup location component activation options
            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(getActivity(), mapStyle)
                    .locationComponentOptions(locationComponentOptions)
                    .build();

            //Activate location component
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            //Enable to make visible
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }else{
            //Show dialog
            ActivityCompat.requestPermissions(getActivity(),
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
    public void onResume() {
        super.onResume();
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
