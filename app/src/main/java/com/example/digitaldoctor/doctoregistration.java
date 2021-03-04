package com.example.digitaldoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class doctoregistration extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView mBack;
    private EditText mName, mEmail, mPhone, mPass, mDob, mLicense, mDept, mSpec, mAddress, mTimefrom, mTimeto;
    private RadioGroup mGender;
    private Button mSignup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctoregistreation);

        mBack = findViewById(R.id.backtoregister);
        mName = findViewById(R.id.docname);
        mEmail = findViewById(R.id.docemail);
        mPhone = findViewById(R.id.docmobile);
        mPass = findViewById(R.id.docpass);
        mDob  = findViewById(R.id.docDOB);
        mLicense = findViewById(R.id.doclicenseno);
        mSpec = findViewById(R.id.docSpeciality);
        mDept = findViewById(R.id.docdepartment);
        mAddress = findViewById(R.id.docAddress);
        mTimefrom = findViewById(R.id.doctimefromnumber);
        mTimeto = findViewById(R.id.doctimetonumber);
        mGender = findViewById(R.id.docRadioGroup);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }


}