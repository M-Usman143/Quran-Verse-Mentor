package com.example.quranmentor.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quranmentor.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;

public class PDF_View_Activity extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdfview);

        String fileName = getIntent().getStringExtra("pdfName");
        if (fileName != null) {
            displayFromAsset(fileName);
        } else {
            Toast.makeText(this, "File name is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayFromAsset(String fileName) {
        try {
            InputStream asset = getAssets().open("PDF/" + fileName);
            pdfView.fromStream(asset).load();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }
}