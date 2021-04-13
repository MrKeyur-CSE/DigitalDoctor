package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class patient_history_log extends AppCompatActivity {

    private TextView back_txt;
    private ListView listView;
    private List<String> nameList = new ArrayList<>();
    private List<String> illList = new ArrayList<>();
    private List<String> p1List = new ArrayList<>();
    private List<String> p2List = new ArrayList<>();
    private List<String> p3List = new ArrayList<>();
    private List<String> p4List = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<String> descriptionList = new ArrayList<>();
    private List<Long> prescriptionNoList = new ArrayList<>();
    private List<String> doctorList = new ArrayList<>();
    private List<String> patientList = new ArrayList<>();


    final FirebaseFirestore pStore = FirebaseFirestore.getInstance();
    CollectionReference dRef = pStore.collection("Prescription");
    CollectionReference patientRef = pStore.collection("Patient");
    Dialog myDialog;
    Bitmap img;
    Long prescriptionNo;
    int po, count = 0;
    String userId, patientname, cmppatientname, description, ill, p1, p2, p3, p4, patientAddress, doctorName, date;
    FirebaseAuth pAuth;
    ImageView download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history_log);
        myDialog = new Dialog(this);

        pAuth = FirebaseAuth.getInstance();
        userId = pAuth.getCurrentUser().getUid();
        DocumentReference pReff = pStore.collection("Patient").document(userId);

        pReff.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                patientname = documentSnapshot.getString("full_name");
                patientAddress = documentSnapshot.getString("address");
            }
        });


        back_txt = findViewById(R.id.back);
        listView = findViewById(R.id.list);

        back_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();
            }
        });

//        final ArrayList<String> list = new ArrayList<>();
//        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, list);
//        listView.setAdapter(adapter);
//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG,OnItemClick)
                po = position;
                String s1 = nameList.get(position);

                QRGEncoder qrgEncoder = new QRGEncoder(s1,null,QRGContents.Type.TEXT,400);
                Bitmap qrBits = qrgEncoder.getBitmap();
                img = qrBits;
                ShowPopup(this, po);
            }
        });

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("record");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Information info = snapshot.getValue(Information.class);
//                    String txt = "Prescription No : "+ info.getPrescriptionNo() + "\nIllness : " + info.getIll()
//                            + "\nMedication : \n - " + info.getP1() + "\n - " + info.getP2() + "\n - " + info.getP3()
//                            + "\n - " + info.getP4();
//                    list.add(txt);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        dRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                nameList.clear();
                illList.clear();
                p1List.clear();
                p2List.clear();
                p3List.clear();
                p4List.clear();
                dateList.clear();
                descriptionList.clear();
                prescriptionNoList.clear();
                doctorList.clear();
                patientList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots) {
                    description = snapshot.getString("discription");
//                    String date = snapshot.getString("date");
                    ill = snapshot.getString("ill");
                    p1 = snapshot.getString("p1");
                    p2 = snapshot.getString("p2");
                    p3 = snapshot.getString("p3");
                    p4 = snapshot.getString("p4");
                    date = snapshot.getString("date");
                    cmppatientname = snapshot.getString("patientname");
                    doctorName = snapshot.getString("doctorname");
                    prescriptionNo = snapshot.getLong("prescriptionNo");
                    String text = "Prescription No : " + prescriptionNo + "\nIllness : " + ill
                            + "\nMedication : \n - " + p1 + "\n - " + p2 + "\n - " + p3
                            + "\n - " + p4 + "\nDescription : " + description;
                    if(cmppatientname.equals(patientname)) {
                        illList.add(ill);
                        p1List.add(p1);
                        p2List.add(p2);
                        p3List.add(p3);
                        p4List.add(p4);
                        dateList.add(date);
                        descriptionList.add(description);
                        prescriptionNoList.add(prescriptionNo);
                        doctorList.add(doctorName);
                        patientList.add(cmppatientname);
                        nameList.add(text);
                    }
                    //nameList.add(snapshot.getString("p1"));
                }
                final ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item, nameList);
                listView.setAdapter(adapter);
            }
        });
    }
    public void ShowPopup(AdapterView.OnItemClickListener v, int po) {
        TextView txtclose;
        ImageView popimg;
        myDialog.setContentView(R.layout.qr_popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        popimg = (ImageView) myDialog.findViewById(R.id.popimage);
        download = myDialog.findViewById(R.id.download);
        txtclose.setText("X");

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printPdf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
//        canvas.drawText("Surat , 395002",20,55,paint);
                forLinePaint.setStyle(Paint.Style.STROKE);
                forLinePaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));
                forLinePaint.setStrokeWidth(2);
                canvas.drawLine(20,65,230,65,forLinePaint);

                canvas.drawText("Patient Name : "+ patientname,20,80,paint);
                canvas.drawLine(20,90,230,90,forLinePaint);

                canvas.drawText("By " +doctorList.get(po),20,105,paint);

                canvas.drawText("Illness : "+illList.get(po),20,125,paint);
                canvas.drawText("Prescription : ",20,145,paint);
                canvas.drawText(" "+p1List.get(po),100,145,paint);
                canvas.drawText(" "+p2List.get(po),100,155,paint);
                canvas.drawText(" "+p3List.get(po),100,165,paint);
                canvas.drawText(" "+p4List.get(po),100,175,paint);
                canvas.drawText("Description : ",20,195,paint);
                canvas.drawText(" "+descriptionList.get(po),20,205,paint);

                canvas.drawLine(20,210,230,210,forLinePaint);

                canvas.drawText("Prescription Number :"+String.valueOf(prescriptionNoList.get(po)),20,225,paint);
                canvas.drawText("Date : "+dateList.get(po),20,240,paint);
                canvas.drawText("Address : "+patientAddress,20,255,paint);

                // canvas.drawText("Date : "+datePatternformat.format(new Date().getTime()),20,260,paint);
                canvas.drawText("Payment Method : Cash",20,290,paint);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(12f);
                canvas.drawText("Get Well Soon!",canvas.getWidth()/2,320,paint);

                myPdfDocument.finishPage(myPage);
//        String url1 = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File file = new File(  Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Presentation.pdf"));
//        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/digidoc/");
//        direct.mkdirs();
//        File file = new File(direct,System.currentTimeMillis()+".pdf");
//        if(!direct.exists()){
//            File walllpaperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/digidoc/");
//            walllpaperDirectory.mkdirs();
//        }

                String s = Environment.getExternalStorageDirectory().toString();
                File file = new File(Environment.getExternalStorageDirectory() + "/Download/Prescription_"+String.valueOf(prescriptionNoList.get(po))+".pdf");

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(ActivityCompat.checkSelfPermission(patient_history_log.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                    //CreateFolder();
                }
                else {
                    ActivityCompat.requestPermissions(patient_history_log.this
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            ,100);
                }

                myPdfDocument.close();
//        String sMes = "error: "+ "\n" + Environment.getExternalStorageDirectory();
//        Toast.makeText(getApplicationContext(),sMes,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Prescription Saved",Toast.LENGTH_SHORT).show();
            }
        });

        popimg.setImageBitmap(img);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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
}