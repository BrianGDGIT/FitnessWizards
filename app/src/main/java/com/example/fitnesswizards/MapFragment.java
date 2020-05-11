package com.example.fitnesswizards;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;


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

    private LocationComponent locationComponent;

    private Pedometer pedometer;

    private MapView mapView;
    private MapboxMap map;
    private Style mapStyle;

    //Map images
    private static final String MARKER_SOURCE = "markers-source";
    private static final String Book_Marker_Image1 = "book_custom-marker";


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

                //Set camera, zoom, rotate, and bounds limits
                map.setMinZoomPreference(18);
                map.getUiSettings().setRotateGesturesEnabled(false);
                map.getUiSettings().setScrollGesturesEnabled(false);
                
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/briancs/ck9m0yeja0lrx1intmbmdus2g"), new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        //Map markers implementation, loading images into map style
                        style.addImage(Book_Marker_Image1, BitmapFactory.decodeResource(MapFragment.this.getResources(), R.drawable.book_02f));
                        addMarkers(style);

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
            this.locationComponent = locationComponent;

            //Setup location component options
            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(getActivity())
                    .foregroundDrawable(R.drawable.wizard)
                    .accuracyAlpha(0)
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
            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        }else{
            //Show dialog
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION);
            enableLocationComponent();
        }



    }

    private void addMarkers(@NonNull Style loadedMapStyle){
        List<Feature> features = new ArrayList<>();

        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        features.add(Feature.fromGeometry(Point.fromLngLat(-121.7847, 37.9901)));

        loadedMapStyle.addSource(new GeoJsonSource(MARKER_SOURCE, FeatureCollection.fromFeatures(features)));

        /* Style layer: A style layer ties together the source and
        image and specifies how they are displayed on the map. */
        loadedMapStyle.addLayer(new SymbolLayer("markers-style-layer", MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconImage(Book_Marker_Image1),
                        // Adjust the second number of the Float array based on the height of your marker image.
                        // This is because the bottom of the marker should be anchored to the coordinate point, rather
                        // than the middle of the marker being the anchor point on the map.
                        PropertyFactory.iconOffset(new Float[] {0f, -52f})
                ));
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
