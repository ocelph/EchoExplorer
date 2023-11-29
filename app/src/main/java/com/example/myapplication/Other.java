package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Other extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        // Button button = (Button) findViewById(R.id.buttonForSave);
        Button button1 = (Button) findViewById(R.id.buttonForShare);
        Button button2 = (Button) findViewById(R.id.buttonForNew);
        Button button3 = (Button) findViewById(R.id.buttonLogOff);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openListedRoutes();
//            }
//        });

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

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOffUser();
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

    private void logOffUser() {

        Intent intent = new Intent(Other.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);

        Toast.makeText(Other.this, "Logged off successfully", Toast.LENGTH_SHORT).show();

        finish();
    }


}