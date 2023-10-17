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


        Button button = (Button) findViewById(R.id.goBackToMain);
        ImageButton button1= (android.widget.ImageButton) findViewById(R.id.favouritesButton);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavourites();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

    }

    public void openFavourites() {
        Intent intent = new Intent(this,ListedRoutes.class);
        startActivity(intent);
    }

    public void openMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}