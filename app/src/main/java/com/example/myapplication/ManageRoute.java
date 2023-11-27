package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageRoute extends AppCompatActivity {

    EditText startlocation;
    EditText endlocation;

    LatLng startlatlng,endlatlng = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);

        Button button1= (Button) findViewById(R.id.startRoutebutton);

        startlocation = findViewById(R.id.startText);
        endlocation = findViewById(R.id.endText);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slocation = startlocation.getText().toString();


                if (slocation == null){
                    Toast.makeText(ManageRoute.this, "Start location not entered", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder startgeocoder = new Geocoder(ManageRoute.this, Locale.CANADA);
                    try {
                        List<Address> slistAddress = startgeocoder.getFromLocationName(slocation,1);
                        if(slistAddress.size() > 0){
                            startlatlng = new LatLng(slistAddress.get(0).getLatitude(),slistAddress.get(0).getLongitude());

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String elocation = endlocation.getText().toString();
                if (elocation == null){
                    Toast.makeText(ManageRoute.this, "End location not entered", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder endgeocoder = new Geocoder(ManageRoute.this, Locale.CANADA);
                    try {
                        List<Address> elistAddress = endgeocoder.getFromLocationName(slocation,1);
                        if(elistAddress.size() > 0){
                            endlatlng = new LatLng(elistAddress.get(0).getLatitude(),elistAddress.get(0).getLongitude());

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                openMap();

            }
        });
    }

    public void openMap() {
        Intent mapintent = new Intent(this,Map.class);
        mapintent.putExtra("Start", startlatlng);
        mapintent.putExtra("End", endlatlng);
        startActivity(mapintent);
    }
}