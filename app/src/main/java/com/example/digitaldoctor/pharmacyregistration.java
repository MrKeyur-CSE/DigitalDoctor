package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class pharmacyregistration extends AppCompatActivity {

    private static final String TAG = "pharmacyRegistration";

    private static final String KEY_PH_LICENSE = "license";
    private static final String KEY_PH_ADDRESS = "address";
    private static final String KEY_PH_TIME_FROM = "time_from";
    private static final String KEY_PH_TIME_TO = "time_to";
    private static final String KEY_PH_NAME = "name_pharmacy";
    private static final String KEY_PH_OWNER = "owner_name";
    private static final String KEY_PH_EMAIL = "email";
    private static final String KEY_PH_PHONE = "ph_no";



    private EditText phName, phEmail, phPass, phPhone, phOwner, phLicense, phAddress, phTimefrom, phTimeto;
    private TextView phBack;
    private Button phSignup;
    private FirebaseAuth phAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyregistration);

        phBack = findViewById(R.id.backtoregister);
        phName = findViewById(R.id.phyname);
        phEmail = findViewById(R.id.phyemail);
        phPhone = findViewById(R.id.phymobile);
        phPass = findViewById(R.id.phypass);
        phOwner = findViewById(R.id.phyownname);
        phLicense = findViewById(R.id.phylicencenumber);
        phAddress = findViewById(R.id.phyAddress);
        phTimefrom = findViewById(R.id.phytimefromnumber);
        phTimeto = findViewById(R.id.phytimetonumber);
        phSignup = findViewById(R.id.loginbutton);

        phSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = phEmail.getText().toString().trim();
                String pass = phPass.getText().toString().trim();
                String phone = phPhone.getText().toString();
                String license = phLicense.getText().toString();
                String address = phAddress.getText().toString();
                String timefrom = phTimefrom.getText().toString();
                String timeto = phTimeto.getText().toString();
                String pharmacy_name = phName.getText().toString();
                String owner_name = phOwner.getText().toString();

                if(TextUtils.isEmpty(email)){
                    phEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    phPass.setError("Password is required");
                    return;
                }
                if(pass.length()<6){
                    phPass.setError("Password must be >= 6 characters");
                    return;
                }

                phAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserId = phAuth.getCurrentUser().getUid();
                            DocumentReference phRef = db.collection("Pharmacy").document(UserId);
                            Map<String, Object> phMap = new HashMap<>();
                            phMap.put(KEY_PH_NAME, pharmacy_name);
                            phMap.put(KEY_PH_EMAIL, email);
                            phMap.put(KEY_PH_PHONE, phone);
                            phMap.put(KEY_PH_LICENSE, license);
                            phMap.put(KEY_PH_ADDRESS, address);
                            phMap.put(KEY_PH_TIME_FROM, timefrom);
                            phMap.put(KEY_PH_TIME_TO, timeto);
                            phMap.put(KEY_PH_OWNER, owner_name);

                            phRef.set(phMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(pharmacyregistration.this, "New Pharmacy Added", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(pharmacyregistration.this, "Registration failed\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), after_login.class));
                        }
                        else{
                            Toast.makeText(pharmacyregistration.this, "Registration failed" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        phBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}