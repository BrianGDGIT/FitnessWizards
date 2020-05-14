package com.example.fitnesswizards;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    //Fragment View
    View view;
    //Permissions constant
    private static final int MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION = 1;

    private static final String TAG = MainActivity.class.getSimpleName();

    private LocationComponent locationComponent;

    private MapView mapView;
    private MapboxMap map;
    private Style mapStyle;

    //Used to save LiveData into player object for other uses
    private PlayerViewModel playerViewModel;
    Player player;

    //Map images
    private static final String BOOK_MARKER_SOURCE = "book-markers-source";
    private static final String BOOK_MARKER_LAYER = "book-markers-style-layer";
    private static final String BOOK_MARKER_IMAGE1 = "book_custom-marker";

    private static final String STAFF_MARKER_SOURCE = "staff-markers-source";
    private static final String STAFF_MARKER_LAYER = "staff-markers-style-layer";
    private static final String STAFF_MARKER_IMAGE1 = "staff_custom-marker";

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

        connectWithData();


        //Mapbox
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                //Save reference to Map for later use
                map = mapboxMap;

                //Set camera, zoom, rotate, and bounds limits
                //map.setMinZoomPreference(18);
                //map.getUiSettings().setRotateGesturesEnabled(false);
                //map.getUiSettings().setScrollGesturesEnabled(false);
                

                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/briancs/ck9m0yeja0lrx1intmbmdus2g"), new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        //Map markers implementation, loading images into map style
                        style.addImage(BOOK_MARKER_IMAGE1, BitmapFactory.decodeResource(MapFragment.this.getResources(), R.drawable.book_02f));
                        style.addImage(STAFF_MARKER_IMAGE1, BitmapFactory.decodeResource(MapFragment.this.getResources(), R.drawable.staff_01a));

                        //Save reference to map style
                        mapStyle = style;

                        enableLocationComponent();
                        addMarkers(mapView, mapboxMap, style);
                    }
                });
            }
        });
        return view;
    }

    private void enableLocationComponent(){
        //Check to see if permissions are enabled
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

    private void addMarkers(MapView mapView, MapboxMap map, Style style){
        SymbolManager symbolManager = new SymbolManager(mapView, map, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setIconIgnorePlacement(true);

        //Control what happens when symbols of different types are clicked
        symbolManager.addClickListener(symbol -> {
            if(symbol.getIconImage() == BOOK_MARKER_IMAGE1){
                Toast.makeText(getApplicationContext(), R.string.clicked_book01, Toast.LENGTH_LONG).show();

                //Display second toast after delay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(), R.string.xp_value100, Toast.LENGTH_LONG).show();
                    }
                }, 2000);

                player.setPlayerExperience(player.getPlayerExperience() + 100);
                playerViewModel.update(player);
                symbolManager.delete(symbol);
            }else{
                Toast.makeText(getApplicationContext(), "Clicked on a staff!", Toast.LENGTH_LONG).show();
            }
        });

        //Generate long and lat and store in a List
        List<Feature> book2Features = new ArrayList<>();
        List<Feature> staff1Features = new ArrayList<>();

        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */

        //Generate Items, returns values for use below
        int amountBook2 = generateItemAmount();
        int amountStaff1 = generateItemAmount();

        //Add Book2 to source
        for (int i = 0; i < amountBook2; i++){
            //Add symbols to map using SymbolManager
            Symbol symbol = symbolManager.create(new SymbolOptions()
                    .withLatLng(generateRandomLatLng())
                    .withIconImage(BOOK_MARKER_IMAGE1)
            );
        }

        //Add Staff1 to source
        for (int i =0; i < amountStaff1; i++){
            //Add symbols to map using SymbolManager
            Symbol symbol = symbolManager.create(new SymbolOptions()
                    .withLatLng(generateRandomLatLng())
                    .withIconImage(STAFF_MARKER_IMAGE1)
            );
        }

    }

    private LatLng generateRandomLatLng(){
        //Generate random longitude and latitude from player location within about 2 miles
        double playerLongitude = locationComponent.getLastKnownLocation().getLongitude();
        double playerLatitude = locationComponent.getLastKnownLocation().getLatitude();

        double randomLongitude = ThreadLocalRandom.current().nextDouble(playerLongitude - 0.0181, playerLongitude + 0.0181);
        double randomLatitude = ThreadLocalRandom.current().nextDouble(playerLatitude - 0.0181, playerLatitude + 0.0181);

        LatLng randomLatLng = new LatLng(randomLatitude, randomLongitude);
        return randomLatLng;
    }

    private int generateItemAmount(){
        return ThreadLocalRandom.current().nextInt(0, 6);
    }

    private void connectWithData(){
        //Connect With data
        playerViewModel = ViewModelProviders.of(this)
                .get(PlayerViewModel.class);

        //Add player data observer
        playerViewModel.getPlayerLiveData().observe(this, new Observer<Player>(){
            @Override
            public void onChanged(Player p) {
                player = p;
            }
        });
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
