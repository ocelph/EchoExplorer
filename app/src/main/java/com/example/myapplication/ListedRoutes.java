package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListedRoutes extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_routes);

        Button button = (Button) findViewById(R.id.goBackToManage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageRoute();
            }
        });
    }

    public void openManageRoute() {
        Intent intent = new Intent(this,ManageRoute.class);
        startActivity(intent);
    }
}