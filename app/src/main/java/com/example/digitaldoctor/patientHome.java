package com.example.digitaldoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class patientHome extends AppCompatActivity {

    TextView pName;
    FirebaseAuth pAuth;
    FirebaseFirestore pStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        pName = findViewById(R.id.pName);
        pAuth = FirebaseAuth.getInstance();
        pStore = FirebaseFirestore.getInstance();

        userId = pAuth.getCurrentUser().getUid();
    }
}