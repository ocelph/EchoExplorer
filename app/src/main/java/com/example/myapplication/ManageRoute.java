package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;

public class ManageRoute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);



        ImageButton button1= (android.widget.ImageButton) findViewById(R.id.favouritesButton);
        Button button2 = (Button) findViewById(R.id.startRoutebutton);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavourites();
            }
        });



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

    }

    public void openFavourites() {
        Intent intent = new Intent(this,ListedRoutes.class);
        startActivity(intent);
    }



    public void openMap() {
        Intent intent = new Intent(this,Map.class);
        startActivity(intent);
    }
}