package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import static androidx.appcompat.app.AlertDialog.*;

public class prescription extends AppCompatActivity {
    // Write a message to the database
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("record");
    FirebaseFirestore fRef = FirebaseFirestore.getInstance();
    CollectionReference cRef = fRef.collection("Prescription");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String uid_doc;
    String doctorName;
    CollectionReference dRef = fRef.collection("Doctor");
    DataObj dataObj = new DataObj();
    Button btnloginbutton;
    EditText discription,ill,p1,p2,p3,p4, date;
    long dpriscriptionNo = 0;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat datePatternformat = new SimpleDateFormat("dd-mm-yyyy hh:mm a");

    private TextView back_txt;
    private TextView log_txt;
    //String sName="Download";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        btnloginbutton = findViewById(R.id.loginbutton);
        p1 = findViewById(R.id.prescription_1);
        p2 = findViewById(R.id.prescription_2);
        p3 = findViewById(R.id.prescription_3);
        p4 = findViewById(R.id.prescription_4);
        ill = findViewById(R.id.name);
        discription = findViewById(R.id.details);
        date = findViewById(R.id.editTextDate_1);
        uid_doc = fAuth.getCurrentUser().getUid();


        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dpriscriptionNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(prescription.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        cRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                    }
                }else{
                    Toast.makeText(prescription.this, "Error" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_txt = findViewById(R.id.back);
        log_txt = findViewById(R.id.log);

        back_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        log_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),history_log.class));
            }
        });


        cRef.document(uid_doc).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                doctorName = documentSnapshot.getString("full_name");
            }
        });

        btnloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataObj.prescriptionNo = count + 1;
                dataObj.ill = String.valueOf(ill.getText());
                dataObj.p1 = String.valueOf(p1.getText());
                dataObj.p2 = String.valueOf(p2.getText());
                dataObj.p3 = String.valueOf(p3.getText());
                dataObj.p4 = String.valueOf(p4.getText());
                dataObj.discription = String.valueOf(discription.getText());
                dataObj.date =String.valueOf(date.getText());

                cRef.document(String.valueOf(count+1)).set(dataObj);

//                myRef.child(String.valueOf(count+1)).setValue(dataObj);

                try {
                    printPdf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //9822204910 9767177341 hp service
            }
        });
    }


    private void printPdf() throws IOException {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,350,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,50,250));

        canvas.drawText("Digital Doctor", 20, 20,paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Address : 101, Sai Residency,",20,40,paint);
        canvas.drawText("Surat , 395002",20,55,paint);
        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20,65,230,65,forLinePaint);

        canvas.drawText("Patient Name : ",20,80,paint);
        canvas.drawLine(20,90,230,90,forLinePaint);

        canvas.drawText("By Dr." +doctorName,20,105,paint);

        canvas.drawText("Illness : "+ill.getText(),20,125,paint);
        canvas.drawText("Prescription : ",20,145,paint);
        canvas.drawText(" "+p1.getText(),100,145,paint);
        canvas.drawText(" "+p2.getText(),100,155,paint);
        canvas.drawText(" "+p3.getText(),100,165,paint);
        canvas.drawText(" "+p4.getText(),100,175,paint);
        canvas.drawText("Description : ",20,195,paint);
        canvas.drawText(" "+discription.getText(),20,205,paint);

        canvas.drawLine(20,210,230,210,forLinePaint);

       // canvas.drawText("Date : "+datePatternformat.format(new Date().getTime()),20,260,paint);
        canvas.drawText("Date : "+String.valueOf(dataObj.date),20,260,paint);
        canvas.drawText(String.valueOf(count+1),20,275,paint);
        canvas.drawText("Payment Method : Cash",20,290,paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Get Well Soon!",canvas.getWidth()/2,320,paint);

        myPdfDocument.finishPage(myPage);
        /* String url1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(  Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Presentation.pdf"));
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/digidoc/");
        direct.mkdirs();
        File file = new File(direct,System.currentTimeMillis()+".pdf");
        if(!direct.exists()){
            File walllpaperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/digidoc/");
            walllpaperDirectory.mkdirs();
        }
        */

        File file = new File(Environment.getExternalStorageDirectory() + "/Download/Prescription_"+String.valueOf(count+1)+".pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ActivityCompat.checkSelfPermission(prescription.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            //CreateFolder();
        }
        else {
            ActivityCompat.requestPermissions(prescription.this
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,100);
        }

        myPdfDocument.close();
//        String sMes = "error: "+ "\n" + Environment.getExternalStorageDirectory();
//        Toast.makeText(getApplicationContext(),sMes,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Prescription Saved",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && (grantResults.length > 0) && (grantResults[0] ==
                PackageManager.PERMISSION_GRANTED)){
            //CreateFolder();
        }
        else {
            Toast.makeText(getApplicationContext(),"Permission Denied ",Toast.LENGTH_SHORT).show();

        }
    }
/*
    private void CreateFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), sName);
        if(file.exists()){
            Toast.makeText(getApplicationContext(),"File already",Toast.LENGTH_SHORT).show();
        }
        else {
            file.mkdir();
            if (file.isDirectory()){
                Toast.makeText(getApplicationContext(),"File created",Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(getApplicationContext(),"File not createdddddd",Toast.LENGTH_SHORT).show();
                String sMes = "errorr: "+ "\n" + Environment.getExternalStorageDirectory();
                Toast.makeText(getApplicationContext(),sMes,Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
}