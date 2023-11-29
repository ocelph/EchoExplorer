package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;

import android.util.Log;
import android.view.Menu;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import android.provider.Settings;

import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.function.Consumer;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissionGranter;
    GoogleMap googleMap;
    Polyline currentPolyline;
    private List<Polyline> polylines = null;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private MarkerDataManager markerDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Button breadCrumbButton = findViewById(R.id.breadCrumb);
        Button intersectionButton = findViewById(R.id.intersection);

        breadCrumbButton.setOnClickListener(v -> dropMarker("BreadCrumb"));
        intersectionButton.setOnClickListener(v -> dropMarker("Intersection"));

        markerDataManager = new MarkerDataManager(this);


        // Initialize map
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button button2 = (Button) findViewById(R.id.other);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Location location = intent.getParcelableExtra("Location");
        String title = intent.getStringExtra("Title");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (location != null && title != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            googleMap.addMarker(markerOptions);
        }

        checkPermission();

        if (isPermissionGranter) {
            if (checkGooglePlayServices()) {

                mapFragment.getMapAsync(this); // will call onMapReady

                Toast.makeText(Map.this, "Google Play Services Available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Map.this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
        }


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOther();
            }
        });
    }

    private void getCurrentLocation(Consumer<LatLng> onLocationReceived) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location
                    if (location != null) {
                        onLocationReceived.accept(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                });
    }


    private void dropMarker(String title) {
        getCurrentLocation(currentLatLng -> {
            if (googleMap != null) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(title));
                markerDataManager.insertMarker(currentLatLng.latitude, currentLatLng.longitude, title);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.NormalMap){
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        }
//
//        if (item.getItemId() == R.id.SatelliteMap){
//            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        }
//
//        if (item.getItemId() == R.id.MapHybrid){
//            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        }
//
//        if (item.getItemId() == R.id.MapTerrain){
//            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(Map.this, "User Cancelled Dialogue", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranter = true;
                Toast.makeText(Map.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    public void openOther() {
        Intent intent = new Intent(this, Other.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        loadMarkers();
        // Getting the data passed through the intent
        Intent mapintent = getIntent();

        List<MarkerOptions> savedMarkers = markerDataManager.getAllMarkers();
        for (MarkerOptions markerOptions : savedMarkers) {
            googleMap.addMarker(markerOptions);
        }
        // Start Location
        LatLng slatlng = mapintent.getParcelableExtra("Start");
        if (slatlng != null) {
            MarkerOptions smarkerOptions = new MarkerOptions();
            smarkerOptions.title("My start position");
            smarkerOptions.position(slatlng);
            googleMap.addMarker(smarkerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(slatlng, 15));
        } else {
            Log.d("Debugging", "Start position data is null");
        }

        // End Location
        LatLng elatlng = mapintent.getParcelableExtra("End");
        if (elatlng != null) {
            MarkerOptions emarkerOptions = new MarkerOptions();
            emarkerOptions.title("My destination");
            emarkerOptions.position(elatlng);
            googleMap.addMarker(emarkerOptions);

            // Polyline Logic
            if (slatlng != null) {
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(slatlng)  // Start Point
                        .add(elatlng)  // End Point
                        .color(Color.RED)  // Set your desired color
                        .width(5);         // Set your desired width
                currentPolyline = googleMap.addPolyline(polylineOptions);
            }
        } else {
            Log.d("Debugging", "End position data is null");
        }

        // Map UI Settings
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request for permissions if not granted
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    private void loadMarkers() {
        List<MarkerOptions> markers = markerDataManager.getAllMarkers();
        for (MarkerOptions marker : markers) {
            googleMap.addMarker(marker);
        }


    }
}