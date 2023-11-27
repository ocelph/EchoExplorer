package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.net.Uri;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
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


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissionGranter;
    GoogleMap googleMap;
    Polyline currentPolyline;
    private List<Polyline> polylines = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        Button button2 = (Button) findViewById(R.id.other);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



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


    public void openActivity2() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openOther() {
        Intent intent = new Intent(this, Other.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;


        Intent mapintent = getIntent();

        LatLng slatlng = mapintent.getParcelableExtra("Start");
        MarkerOptions smakerOptions = new MarkerOptions();

        smakerOptions.title("My start position");
        Log.d("Debugging", "Start posi" + slatlng.toString());
        smakerOptions.position(slatlng);
        googleMap.addMarker(smakerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(slatlng, 15);
        googleMap.animateCamera(cameraUpdate);

        // site complex location
        LatLng latlng = new LatLng(45.4194, -75.6786);
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.title("Test");
        makerOptions.position(latlng);
        googleMap.addMarker(makerOptions);

        LatLng elatlng = mapintent.getParcelableExtra("End");
        MarkerOptions emakerOptions = new MarkerOptions();

        emakerOptions.title("My destination");
        Log.d("Debugging", "End posi" + elatlng.toString());
        emakerOptions.position(elatlng);
        googleMap.addMarker(emakerOptions);

        if (currentPolyline != null){
            currentPolyline.remove();
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(slatlng)  // Point 1
                    .add(latlng)  // Point 2
                    .color(Color.RED)  // Set your desired color
                    .width(5);         // Set your desired width
            currentPolyline = googleMap.addPolyline(polylineOptions);
        } else {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(slatlng)  // Point 1
                    .add(latlng)  // Point 2
                    .color(Color.RED)  // Set your desired color
                    .width(5);         // Set your desired width
            currentPolyline = googleMap.addPolyline(polylineOptions);
        }


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

}