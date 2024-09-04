package com.example.quranmentor.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.PdfAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.models.PdfItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TajweedActivity extends AppCompatActivity {
    private RecyclerView pdfRecyclerView;
    private PdfAdapter pdfAdapter;
    private List<PdfItem> pdfList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tajweed);
        pdfRecyclerView = findViewById(R.id.pdfRecyclerView);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pdfList = getPDFFilesFromAssets();

        pdfAdapter = new PdfAdapter(pdfList, this);
        pdfRecyclerView.setAdapter(pdfAdapter);
    }
    private List<PdfItem> getPDFFilesFromAssets() {
        List<PdfItem> pdfFiles = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            String[] files = assetManager.list("PDF");
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".pdf")) {
                        pdfFiles.add(new PdfItem(file));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFiles;
    }
    }
