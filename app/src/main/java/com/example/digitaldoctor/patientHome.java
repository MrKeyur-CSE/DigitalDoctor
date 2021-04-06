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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class patientHome extends AppCompatActivity {

    private TextView patientbacktologin;
    private ImageView log;
    private ListView listView;
    private List<String> nameList = new ArrayList<>();
    private List<String> specList = new ArrayList<>();
    private List<String> numList = new ArrayList<>();
    private List<String> addList = new ArrayList<>();
    final FirebaseFirestore pStore = FirebaseFirestore.getInstance();
    CollectionReference dRef = pStore.collection("Doctor");
    ImageView gotopatientinfo;
    ImageView search;
    Dialog myDialog;
    String s1,s2,s3,s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        myDialog = new Dialog(this);

        gotopatientinfo = findViewById(R.id.gotopatientinfo);
        gotopatientinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),patient_home_info.class));
            }
        });

        patientbacktologin = findViewById(R.id.patientbacktologin);
        patientbacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = findViewById(R.id.listofpatient);
        pStore.collection("Doctor").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                nameList.clear();
                specList.clear();
                numList.clear();
                addList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots){
                    nameList.add(snapshot.getString("full_name"));
                    specList.add(snapshot.getString("speciality"));
                    numList.add(snapshot.getString("ph_no"));
                    addList.add(snapshot.getString("address"));
                }
                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item,nameList);
                listView.setAdapter(adapter);
            }
        });

        log = findViewById(R.id.log);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),history_log.class));
            }
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),search_doctor.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                s1 = nameList.get(position);
                s2 = specList.get(position);
                s3 = numList.get(position);
                s4 = addList.get(position);

                Toast.makeText(getApplicationContext(),"This is "+ s1,Toast.LENGTH_SHORT).show();
                ShowPopup(this);
            }
        });
    }
    public void ShowPopup(AdapterView.OnItemClickListener v) {
        myDialog.setContentView(R.layout.docinfo_popup);

        TextView txtclose;
        TextView docname;
        TextView docspec;
        TextView docnum;
        TextView docadd;

        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        docname =(TextView) myDialog.findViewById(R.id.docname);
        docspec =(TextView) myDialog.findViewById(R.id.docspec);
        docnum =(TextView) myDialog.findViewById(R.id.docnum);
        docadd =(TextView) myDialog.findViewById(R.id.docadd);

        txtclose.setText("X");
        docname.setText(s1);
        docspec.setText(s2);
        docnum.setText(s3);
        docadd.setText(s4);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}