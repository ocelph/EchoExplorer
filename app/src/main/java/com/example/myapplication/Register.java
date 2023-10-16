package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;

import android.view.View;

import android.content.Intent;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = (Button) findViewById(R.id.register);
        Button goBackLogin = (Button) findViewById(R.id.btnGoBackLogin);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }

    public void openMap() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}