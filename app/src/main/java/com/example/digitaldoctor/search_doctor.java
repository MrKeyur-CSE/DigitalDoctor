package com.example.digitaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class search_doctor extends AppCompatActivity {

    SearchView searchview;
    ListView mylist;
    ArrayList<String> mainlist;
    ArrayAdapter<String> adapter;
    private List<String> specList = new ArrayList<>();
    private List<String> numList = new ArrayList<>();
    private List<String> addList = new ArrayList<>();
    final FirebaseFirestore pStore = FirebaseFirestore.getInstance();
    String s1,s2,s3,s4;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        myDialog = new Dialog(this);
        searchview = findViewById(R.id.searchview);
        mylist = findViewById(R.id.mylist);
        mainlist = new ArrayList<String>();

        pStore.collection("Doctor").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                mainlist.clear();
                specList.clear();
                numList.clear();
                addList.clear();
                for (DocumentSnapshot snapshot : documentSnapshots) {
                    mainlist.add(snapshot.getString("full_name"));
                    specList.add(snapshot.getString("speciality"));
                    numList.add(snapshot.getString("ph_no"));
                    addList.add(snapshot.getString("address"));
                }
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainlist);
        mylist.setAdapter(adapter);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                s1 = mainlist.get(position);
                s2 = specList.get(position);
                s3 = numList.get(position);
                s4 = addList.get(position);

//                Toast.makeText(getApplicationContext(),"This is "+ position,Toast.LENGTH_SHORT).show();
                ShowPopup(this);
            }
        });

    }
    public void ShowPopup(AdapterView.OnItemClickListener v) {
        myDialog.setContentView(R.layout.docinfo_popup);

        TextView txtclose,docname,docspec,docnum,docadd;
        ImageView call;
        ImageView location;

        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        docname =(TextView) myDialog.findViewById(R.id.docname);
        docspec =(TextView) myDialog.findViewById(R.id.docspec);
        docnum =(TextView) myDialog.findViewById(R.id.docnum);
        docadd =(TextView) myDialog.findViewById(R.id.docadd);
        call = (ImageView) myDialog.findViewById(R.id.call);
        location = (ImageView) myDialog.findViewById(R.id.location);

        txtclose.setText("X");
        docname.setText(s1);
        docspec.setText(s2);
        docnum.setText(s3);
        docadd.setText(s4);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+s3));
                startActivity(callIntent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://www.google.ca/maps/place/" + s4 ;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

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