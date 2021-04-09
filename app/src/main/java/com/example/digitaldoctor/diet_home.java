package com.example.digitaldoctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class diet_home extends AppCompatActivity {

    TextView dietishere, age, back, height, weight, bmidisplay;
    RadioGroup diet;
    RadioButton dietbutton_p;
    String s,h,w;
    int b,h1,w1;
    Button getdiet;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_home);

//        bmidisplay = findViewById(R.id.bmidisplay);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dietishere = findViewById(R.id.dietishere);

//        Intent intent = getIntent();
//        String height = intent.getStringExtra("height");
//        String weight = intent.getStringExtra("weight");
//        String diet = intent.getStringExtra("diet");
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        diet = findViewById(R.id.VegGroup);
        age = (TextView) findViewById(R.id.age);

        getdiet = (Button)findViewById(R.id.getdiet);
        getdiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = age.getText().toString();
                h = height.getText().toString();
                w = weight.getText().toString();

                int DietId = diet.getCheckedRadioButtonId();
                dietbutton_p = findViewById(DietId);
                String dietprint = dietbutton_p.getText().toString();

                OkHttpClient client = new OkHttpClient();
                url = "https://diet-recommendation.herokuapp.com/"+s+"/"+w+"/"+h+"/"+dietprint;
                Toast.makeText(getApplicationContext(),"PROCESSING >>>>>", Toast.LENGTH_SHORT).show();
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if(response.isSuccessful()){
                            String myresponse = response.body().string();
                            diet_home.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dietishere.setText(myresponse);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}