package com.example.digitaldoctor;

import android.Manifest;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


public class Scanned_qr extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanned_qr);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.resultOfQr);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultData.setText(result.getText());
                            //Toast.makeText(Scanned_qr.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}
