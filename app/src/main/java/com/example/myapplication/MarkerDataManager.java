package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MarkerDataManager {
    private SQLiteDatabase db;
    private MarkerDBHelper dbHelper;

    public MarkerDataManager(Context context) {
        dbHelper = new MarkerDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertMarker(double latitude, double longitude, String title) {
        ContentValues values = new ContentValues();
        values.put(MarkerDBHelper.COLUMN_LATITUDE, latitude);
        values.put(MarkerDBHelper.COLUMN_LONGITUDE, longitude);
        values.put(MarkerDBHelper.COLUMN_TITLE, title);
        db.insert(MarkerDBHelper.TABLE_MARKERS, null, values);
    }

    public List<MarkerOptions> getAllMarkers() {
        List<MarkerOptions> markers = new ArrayList<>();
        Cursor cursor = db.query(MarkerDBHelper.TABLE_MARKERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int latitudeIndex = cursor.getColumnIndex(MarkerDBHelper.COLUMN_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(MarkerDBHelper.COLUMN_LONGITUDE);
            int titleIndex = cursor.getColumnIndex(MarkerDBHelper.COLUMN_TITLE);

            // Check if indices are valid
            if (latitudeIndex != -1 && longitudeIndex != -1 && titleIndex != -1) {
                do {
                    double latitude = cursor.getDouble(latitudeIndex);
                    double longitude = cursor.getDouble(longitudeIndex);
                    String title = cursor.getString(titleIndex);

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(title);
                    markers.add(markerOptions);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return markers;
    }



}
