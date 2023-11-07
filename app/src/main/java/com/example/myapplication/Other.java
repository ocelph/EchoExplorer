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
                openRecord();
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

    public void openRecord() {
        Intent intent = new Intent(this, Record.class);
        startActivity(intent);
    }


}