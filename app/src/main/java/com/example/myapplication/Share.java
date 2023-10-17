package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Share extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Button button = (Button) findViewById(R.id.buttonGoBackOther);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOther();
            }
        });
    }


    public void openOther() {
        Intent intent = new Intent(this, Other.class);
        startActivity(intent);
    }
}