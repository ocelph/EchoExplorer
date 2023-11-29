package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {
    private LatLng location;
    private String title;

    public MarkerInfo(LatLng location, String title) {
        this.location = location;
        this.title = title;
    }
    
    public LatLng getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
}
