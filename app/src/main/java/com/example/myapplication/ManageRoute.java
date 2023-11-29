package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ManageRoute extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    EditText startlocation;
    EditText endlocation;

    LatLng startlatlng, endlatlng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        Button button1 = findViewById(R.id.startRoutebutton);

        startlocation = findViewById(R.id.startText);
        endlocation = findViewById(R.id.endText);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fieldsEmpty = startlocation.getText().toString().isEmpty() && endlocation.getText().toString().isEmpty();

                if (!fieldsEmpty) {
                    startlatlng = getLocationFromAddress(startlocation.getText().toString(), "Start");
                    endlatlng = getLocationFromAddress(endlocation.getText().toString(), "End");
                }

                openMap(fieldsEmpty);
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = location;
            }
        });
    }

    // ... getLocationFromAddress and other methods remain the same ...

    public void openMap(boolean zoomToCurrentLocation) {
        Intent mapintent = new Intent(this, Map.class);
        if (zoomToCurrentLocation && currentLocation != null) {
            mapintent.putExtra("CurrentLocation", new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        } else {
            if (startlatlng != null) {
                mapintent.putExtra("Start", startlatlng);
            }
            if (endlatlng != null) {
                mapintent.putExtra("End", endlatlng);
            }
        }
        startActivity(mapintent);
    }

    private LatLng getLocationFromAddress(String addressString, String locationType) {
        if (addressString.isEmpty()) {
            return null; // Return null if the address string is empty
        }

        Geocoder geocoder = new Geocoder(ManageRoute.this, Locale.CANADA);
        try {
            List<Address> listAddress = geocoder.getFromLocationName(addressString, 1);
            if (listAddress.size() > 0) {
                return new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
            } else {
                Toast.makeText(ManageRoute.this, locationType + " location not found", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (IOException e) {
            Toast.makeText(ManageRoute.this, "Error finding " + locationType + " location", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void openMap() {
        Intent mapintent = new Intent(this, Map.class);
        if (startlatlng != null) {
            mapintent.putExtra("Start", startlatlng);
        }
        if (endlatlng != null) {
            mapintent.putExtra("End", endlatlng);
        }
        startActivity(mapintent);
    }
}
