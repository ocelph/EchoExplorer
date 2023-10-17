package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Other extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Button button = (Button) findViewById(R.id.buttonForSave);
        Button button1 = (Button) findViewById(R.id.buttonForShare);
        Button button2 = (Button) findViewById(R.id.buttonForNew);
        Button button3 = (Button) findViewById(R.id.buttonForBack);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListedRoutes();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
    }


    public void openListedRoutes() {
        Intent intent = new Intent(this, ListedRoutes.class);
        startActivity(intent);
    }

    public void openShare() {
        Intent intent = new Intent(this, Share.class);
        startActivity(intent);
    }

    public void openMap() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }


}