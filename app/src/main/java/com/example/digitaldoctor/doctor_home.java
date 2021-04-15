 package com.example.digitaldoctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class doctor_home extends AppCompatActivity {

    ImageView img;
    ImageView gotoinfo;
    TextView docbacktologin;
    private ListView listView;
    private List<String> nameList = new ArrayList<>();
    final FirebaseFirestore pStore = FirebaseFirestore.getInstance();
//    CollectionReference dRef = pStore.collection("Patient");
//    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        gotoinfo = findViewById(R.id.gotoinfo);
        gotoinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),doc_home_info.class));
            }
        });

        img =(ImageView) findViewById(R.id.log);
        img.bringToFront();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Scanned_qr.class));
                Intent myIntent = new Intent(doctor_home.this, history_log.class);
                startActivity(myIntent);
            }
        });

        docbacktologin = findViewById(R.id.docbacktologin);
        docbacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = findViewById(R.id.listofdoctor);
        pStore.collection("Patient").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                nameList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots){
                    nameList.add(snapshot.getString("full_name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item,nameList);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s1 = nameList.get(position);
                Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(doctor_home.this,prescription.class);
                i.putExtra("Name",s1);
                startActivity(i);
//                startActivity(new Intent(getApplicationContext(),prescription.class));
            }
        });

    }
}