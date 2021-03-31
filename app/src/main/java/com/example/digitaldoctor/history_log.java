package com.example.digitaldoctor;

import androidx.annotation.NonNull;
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
import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class history_log extends AppCompatActivity {

    private TextView back_txt;
    private ListView listView;
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

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG,OnItemClick)
                String s1 = list.get(position);
                //Toast.makeText(getApplicationContext(),"You click "+ s1,Toast.LENGTH_SHORT).show();

                QRGEncoder qrgEncoder = new QRGEncoder(s1,null,QRGContents.Type.TEXT,400);
                Bitmap qrBits = qrgEncoder.getBitmap();
                img = qrBits;
                ShowPopup(this);


            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("record");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Information info = snapshot.getValue(Information.class);
                    String txt = "Prescription No : "+ info.getPrescriptionNo() + "\nIllness : " + info.getIll()
                            + "\nMedication : \n - " + info.getP1() + "\n - " + info.getP2() + "\n - " + info.getP3()
                            + "\n - " + info.getP4();
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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