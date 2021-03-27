package com.example.digitaldoctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class patientHome extends AppCompatActivity {

    TextView pName;
    FirebaseAuth pAuth;
    FirebaseFirestore pStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);


        pAuth = FirebaseAuth.getInstance();
        pStore = FirebaseFirestore.getInstance();

        userId = pAuth.getCurrentUser().getUid();
        DocumentReference pReff = pStore.collection("Patient").document(userId);

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                pName.setText(documentSnapshot.getString("full_name"));
            }
        });
    }
}