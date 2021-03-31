package com.example.digitaldoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class doctor_home extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        img =(ImageView) findViewById(R.id.add);
        img.bringToFront();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Scanned_qr.class));
                Intent myIntent = new Intent(doctor_home.this, prescription.class);
                startActivity(myIntent);
            }
        });

    }
}