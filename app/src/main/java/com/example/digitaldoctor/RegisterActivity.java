package com.example.digitaldoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private Button mDoctor, mPharmacy, mPatient;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mDoctor = findViewById(R.id.doctor);
        mPharmacy = findViewById(R.id.pharmacy);
        mPatient = findViewById(R.id.patient);
        back = findViewById(R.id.back);

        mDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),));
            }
        });

        mPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),));
            }
        });

        mPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

    }
}