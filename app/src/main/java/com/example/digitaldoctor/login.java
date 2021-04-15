package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLogin;
    private TextView mRegister, mBack;
    private FirebaseAuth mAuth;
    private String UserId;
    private FirebaseFirestore mStore;
    private RadioGroup loginAs;
    private RadioButton category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.loginid);
        mPassword = findViewById(R.id.login_password);
        mLogin = findViewById(R.id.loginbutton);
        mRegister = findViewById(R.id.Register);
        mBack = findViewById(R.id.back);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        loginAs = findViewById(R.id.category);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();

                int RadioId = loginAs.getCheckedRadioButtonId();
                category = findViewById(RadioId);
                String cat = category.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPassword.setError("Password is required");
                    return;
                }
                if(pass.length()<6){
                    mPassword.setError("Password must be >= 6 characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            Toast.makeText(login.this, "Logged In",Toast.LENGTH_SHORT).show();
                            UserId = mAuth.getCurrentUser().getUid();
                            DocumentReference docRef = mStore.collection(cat).document(UserId);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            //Toast.makeText(login.this, "Welcome "+cat, Toast.LENGTH_SHORT).show();
                                            if (cat.equals("Patient")){
//                                                Toast.makeText(login.this, "Test "+cat, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),patientHome.class));
                                            }
                                            else if(cat.equals("Doctor")){
                                                startActivity(new Intent(getApplicationContext(),doctor_home.class));
                                            }
                                           else if(cat.equals("Pharmacy")) {

                                                if(ActivityCompat.checkSelfPermission(login.this, Manifest.permission.CAMERA)
                                                        == PackageManager.PERMISSION_GRANTED){
                                                    startActivity(new Intent(getApplicationContext(),Scanned_qr.class));
                                                }
                                                else {
                                                    ActivityCompat.requestPermissions(login.this
                                                            , new String[]{Manifest.permission.CAMERA}
                                                            ,100);
                                                }
                                           }
                                        } else {
                                            Toast.makeText(login.this, "User isn't registered in this category", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else{
                            Toast.makeText(login.this, "Error!\n"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}