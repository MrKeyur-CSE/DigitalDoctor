package com.example.digitaldoctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class patient_home_info extends AppCompatActivity {


    private TextView back_txt,patientname,patientemail,patientblood,patientmobileno,patientdiet;
    String userId;
    FirebaseAuth pAuth;
    FirebaseFirestore pStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_info);

        patientname = findViewById(R.id.patientname);
        patientemail =findViewById(R.id.patientemail);
        patientblood =findViewById(R.id.patientblood);
        patientmobileno =findViewById(R.id.patientmobileno);
        patientdiet =findViewById(R.id.patientdiet);

        pAuth = FirebaseAuth.getInstance();
        pStore = FirebaseFirestore.getInstance();
        userId = pAuth.getCurrentUser().getUid();
        DocumentReference pReff = pStore.collection("Patient").document(userId);

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientname.setText(documentSnapshot.getString("full_name"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientemail.setText(documentSnapshot.getString("email"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientblood.setText(documentSnapshot.getString("blood_group"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientmobileno.setText(documentSnapshot.getString("phone_no"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientdiet.setText(documentSnapshot.getString("diet"));
            }
        });

        back_txt = findViewById(R.id.backtopatienthome);
        back_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}