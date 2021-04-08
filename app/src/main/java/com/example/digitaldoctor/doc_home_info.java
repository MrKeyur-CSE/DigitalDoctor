package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class doc_home_info extends AppCompatActivity {

    private TextView docinfoBack;
    private TextView docname;
    private TextView docemail;
    private TextView doclicenseno;
    private TextView docmobileno;
    private TextView docspeciality;
    Button doclogout;
    String userId;
    FirebaseAuth pAuth;
    FirebaseFirestore pStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_home_info);

        docname = findViewById(R.id.docname);
        docemail = findViewById(R.id.docemail);
        doclicenseno = findViewById(R.id.doclicenseno);
        docmobileno = findViewById(R.id.docmobileno);
        docspeciality = findViewById(R.id.docspeciality);
        pAuth = FirebaseAuth.getInstance();
        pStore = FirebaseFirestore.getInstance();

        userId = pAuth.getCurrentUser().getUid();
        DocumentReference pReff = pStore.collection("Doctor").document(userId);

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                docname.setText(documentSnapshot.getString("full_name"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                docemail.setText(documentSnapshot.getString("email"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                docmobileno.setText(documentSnapshot.getString("ph_no"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                doclicenseno.setText(documentSnapshot.getString("license"));
            }
        });

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                docspeciality.setText(documentSnapshot.getString("speciality"));
            }
        });

        docinfoBack = findViewById(R.id.backtodochome);
        docinfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doclogout = (Button)findViewById(R.id.doclogout);
        doclogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}