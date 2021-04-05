package com.example.digitaldoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class doctor_home extends AppCompatActivity {

    ImageView img;
    ImageView gotoinfo;
    TextView docbacktologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        gotoinfo = findViewById(R.id.gotoinfo);
        gotoinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),doc_home_info.class));
            }
        });

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

        docbacktologin = findViewById(R.id.docbacktologin);
        docbacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}