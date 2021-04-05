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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class patientregistration extends AppCompatActivity {

    private static final String TAG = "patientRegister";

    private static final String KEY_P_NAME = "full_name";
    private static final String KEY_P_EMAIL = "email";
    private static final String KEY_P_PHONE = "phone_no";
    private static final String KEY_P_DOB = "dob";
    private static final String KEY_P_BLOOD = "blood_group";
    private static final String KEY_P_ADDRESS = "address";
    private static final String KEY_P_HEIGHT = "height_cm";
    private static final String KEY_P_WEIGHT = "weight_kg";
    private static final String KEY_P_GENDER = "gender";
    private static final String KEY_P_DIET = "diet";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView pBack;
    private EditText pName, pEmail, pPass, pPhone, pDob, pBlood, pAddress, pHeight, pWeight;
    private RadioGroup pGender, pDiet;
    private RadioButton genderbutton_p, dietbutton_p;
    private Button pSignup;

    String UserId;

    private FirebaseAuth pAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientregistration);

        pBack = findViewById(R.id.backtoregister);
        pName = findViewById(R.id.patname);
        pEmail = findViewById(R.id.patemail);
        pPass = findViewById(R.id.patpass);
        pPhone = findViewById(R.id.patmobile);
        pDob = findViewById(R.id.patDOB);
        pBlood = findViewById(R.id.patbloodgroup);
        pAddress = findViewById(R.id.patAddress);
        pHeight = findViewById(R.id.heightnumber);
        pWeight = findViewById(R.id.weightnumber);
        pGender = findViewById(R.id.patRadioGroup);
        pDiet = findViewById(R.id.VegGroup);
        pSignup = findViewById(R.id.loginbutton);


        pSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = pEmail.getText().toString().trim();
                String pass = pPass.getText().toString().trim();
                String name = pName.getText().toString();
                String phone = pPhone.getText().toString();
                String dob = pDob.getText().toString();
                String blood = pBlood.getText().toString();
                String address = pAddress.getText().toString();
                String height = pHeight.getText().toString();
                String weight = pWeight.getText().toString();
                int GenderId = pGender.getCheckedRadioButtonId();
                genderbutton_p = findViewById(GenderId);
                String gender = genderbutton_p.getText().toString();
                int DietId = pDiet.getCheckedRadioButtonId();
                dietbutton_p = findViewById(DietId);
                String diet = dietbutton_p.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    pEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    pPass.setError("Password is required");
                    return;
                }
                if (pass.length() < 6) {
                    pPass.setError("Password must be >= 6 characters");
                    return;
                }

                pAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserId = pAuth.getCurrentUser().getUid();
                            DocumentReference pRef = db.collection("Patient").document(UserId);
                            Map<String, Object> pMap = new HashMap<>();
                            pMap.put(KEY_P_NAME, name);
                            pMap.put(KEY_P_EMAIL, email);
                            pMap.put(KEY_P_PHONE, phone);
                            pMap.put(KEY_P_DOB, dob);
                            pMap.put(KEY_P_BLOOD, blood);
                            pMap.put(KEY_P_ADDRESS, address);
                            pMap.put(KEY_P_HEIGHT, height);
                            pMap.put(KEY_P_WEIGHT, weight);
                            pMap.put(KEY_P_GENDER, gender);
                            pMap.put(KEY_P_DIET, diet);
                            //pMap.put("tag", "patient");

                            pRef.set(pMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(patientregistration.this, "New User saved as Patient", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(patientregistration.this, "Registration failed\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), login.class));
                        } else {
                            Toast.makeText(patientregistration.this, "Registration failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




        pBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}