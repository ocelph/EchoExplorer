package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;

import android.view.View;

import android.content.Intent;
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1= (Button) findViewById(R.id.mainLoginBtn);
        Button button2= (Button) findViewById(R.id.mainRegisterBtn);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });




    }


    public void openLogin() {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    public void openRegister() {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }



}