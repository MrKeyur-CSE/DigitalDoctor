package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class history_log extends AppCompatActivity {

    private TextView back_txt;
    private ListView listView;
    private List<String> nameList = new ArrayList<>();
    final FirebaseFirestore pStore = FirebaseFirestore.getInstance();
    CollectionReference dRef = pStore.collection("Prescription");
    ImageView imageView;
    Dialog myDialog;
    Bitmap img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);
        myDialog = new Dialog(this);


        back_txt = findViewById(R.id.back);
        listView = findViewById(R.id.list);
        imageView = findViewById(R.id.popimage);

        back_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),prescription.class));
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
                String s1 = nameList.get(position);
                //Toast.makeText(getApplicationContext(),"You click "+ s1,Toast.LENGTH_SHORT).show();

                QRGEncoder qrgEncoder = new QRGEncoder(s1,null,QRGContents.Type.TEXT,400);
                Bitmap qrBits = qrgEncoder.getBitmap();
                img = qrBits;
                ShowPopup(this);
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

        pStore.collection("Prescription").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                nameList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots){
                    String description = snapshot.getString("discription");
//                    String date = snapshot.getString("date");
                    String ill = snapshot.getString("ill");
                    String p1 = snapshot.getString("p1");
                    String p2 = snapshot.getString("p2");
                    String p3 = snapshot.getString("p3");
                    String p4 = snapshot.getString("p4");
                    Long prescriptionNo = snapshot.getLong("prescriptionNo");
                    String text = "Prescription No : "+prescriptionNo+  "\nIllness : " + ill
                            + "\nMedication : \n - " + p1 + "\n - " + p2 + "\n - " + p3
                            + "\n - " + p4 + "\nDescription : " + description;
                    nameList.add(text);

                    //nameList.add(snapshot.getString("p1"));
                }
                final ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item, nameList);
                listView.setAdapter(adapter);
            }
        });
    }
    public void ShowPopup(AdapterView.OnItemClickListener v) {
        TextView txtclose;
        ImageView popimg;
        myDialog.setContentView(R.layout.qr_popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        popimg = (ImageView) myDialog.findViewById(R.id.popimage);
        txtclose.setText("X");

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        popimg.setImageBitmap(img);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}