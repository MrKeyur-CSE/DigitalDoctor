package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class doctoregistration extends AppCompatActivity {

    private static final String TAG = "doctorRegister";

    private static final String KEY_D_LICENSE = "license";
    private static final String KEY_D_DEPART = "depart";
    private static final String KEY_D_SPEC = "speciality";
    private static final String KEY_D_ADDRESS = "address";
    private static final String KEY_D_TIME_FROM = "time_from";
    private static final String KEY_D_TIME_TO = "time_to";

    static final String KEY_D_NAME = "full_name";
    static final String KEY_D_EMAIL = "email";
    static final String KEY_D_PHONE = "ph_no";
    static final String KEY_D_DOB = "dob";
    static final String KEY_D_GENDER = "gender";

    int year, month, day;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView dBack;
    private EditText dName, dEmail, dPhone, dPass, dDob, dLicense, dDept, dSpec, dAddress, dTimefrom, dTimeto;
    private RadioGroup dGender;
    private RadioButton genderButton_d;
    private Button dSignup;

    String UserId;

    private FirebaseAuth dAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctoregistreation);

        dBack = findViewById(R.id.backtoregister);
        dEmail = findViewById(R.id.docemail);
        dPhone = findViewById(R.id.docmobile);
        dPass = findViewById(R.id.docpass);
        dSignup = findViewById(R.id.loginbutton);
        dDob  = findViewById(R.id.docDOB);
        dLicense = findViewById(R.id.doclicenseno);
        dSpec = findViewById(R.id.docSpeciality);
        dDept = findViewById(R.id.docdepartment);
        dAddress = findViewById(R.id.docAddress);
        dTimefrom = findViewById(R.id.doctimefromnumber);
        dTimeto = findViewById(R.id.doctimetonumber);
        dGender = findViewById(R.id.docRadioGroup);
        dName = findViewById(R.id.docname);

        final Calendar calendar = Calendar.getInstance();
        dDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dDob.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });

        dDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(calendar.YEAR);
                month = calendar.get(calendar.MONTH);
                day = calendar.get(calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(doctoregistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dDob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        dSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = dEmail.getText().toString().trim();
                String pass = dPass.getText().toString().trim();
                String phone = dPhone.getText().toString();
                String dob = dDob.getText().toString();
                String license = dLicense.getText().toString();
                String dept = dDept.getText().toString();
                String spec = dSpec.getText().toString();
                String address = dAddress.getText().toString();
                String timefrom = dTimefrom.getText().toString();
                String timeto = dTimeto.getText().toString();
                int RadioId = dGender.getCheckedRadioButtonId();
                genderButton_d = findViewById(RadioId);
                String gender = genderButton_d.getText().toString();
                String name = dName.getText().toString();

                if(TextUtils.isEmpty(email)){
                    dEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    dPass.setError("Password is required");
                    return;
                }
                if(pass.length()<6){
                    dPass.setError("Password must be >= 6 characters");
                    return;
                }

                dAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserId = dAuth.getCurrentUser().getUid();
                            DocumentReference dRef = db.collection("Doctor").document(UserId);
                            Map<String, Object> dMap = new HashMap<>();
                            dMap.put(KEY_D_NAME, name);
                            dMap.put(KEY_D_EMAIL, email);
                            dMap.put(KEY_D_PHONE, phone);
                            dMap.put(KEY_D_DOB, dob);
                            dMap.put(KEY_D_LICENSE, license);
                            dMap.put(KEY_D_DEPART, dept);
                            dMap.put(KEY_D_SPEC, spec);
                            dMap.put(KEY_D_ADDRESS, address);
                            dMap.put(KEY_D_TIME_FROM, timefrom);
                            dMap.put(KEY_D_TIME_TO, timeto);
                            dMap.put(KEY_D_GENDER, gender);
                            //dMap.put("tag", "doctor");

                            dRef.set(dMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(doctoregistration.this, "New User saved as Doctor", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(doctoregistration.this, "Registration failed\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), login.class));
                        }
                        else{
                            Toast.makeText(doctoregistration.this, "Registration failed" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}




