package com.example.quranmentor.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.SurahDetailAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.common.completeTranslation;
import com.example.quranmentor.models.Surah;
import com.example.quranmentor.models.SurahDetail;
import com.example.quranmentor.network.ApiClientDetail_Quran;
import com.example.quranmentor.network.ApiInterface;
import com.example.quranmentor.response.SurahDetailResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class completeSurah extends AppCompatActivity {
    private TextView surahName , surahType , ayatnumber , bistranslation , bistextview;
    private int  no;
    private String urdu  ="urdu_junagarhi";
    RecyclerView detailrecyclerView;
    private List<SurahDetail> surahDetails;
    private List<Surah> surahDetailss;
    private SurahDetailAdapter surahDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_surah);

        surahName = findViewById(R.id.surahnames);
        surahType = findViewById(R.id.relavation);
        ayatnumber = findViewById(R.id.Ayaatnumber);
        bistranslation = findViewById(R.id.bisTranslation);
        bistextview = findViewById(R.id.bismillahtextview);
        detailrecyclerView = findViewById(R.id.completesurahDEtail);
        DATA();
    }
    private void DATA(){

    no = getIntent().getIntExtra(completeTranslation.SURAH_NO , 0);
    surahName.setText(getIntent().getStringExtra(completeTranslation.SURAH_NAME));
    surahType.setText(getIntent().getStringExtra(completeTranslation.SURAH_TYPE));
    int totalAya = getIntent().getIntExtra(completeTranslation.SURAH_TOTALAYA, 0);
    ayatnumber.setText(String.valueOf(totalAya));
    detailrecyclerView.setHasFixedSize(true);
    detailrecyclerView.setLayoutManager(new LinearLayoutManager(this));
    surahDetails = new ArrayList<>();
    surahDetailAdapter = new SurahDetailAdapter(surahDetails, this);
    detailrecyclerView.setAdapter(surahDetailAdapter);
    fetchSurahData(urdu, no);

    surahDetailss = populateSurahsList();
    setupBismillahTextView();
    displaySurah(no);
    Typeface urduFont = Typeface.createFromAsset(getAssets(), "fonts/Jameel_Noori_Nastaleeq_Kasheeda.ttf");
    bistranslation.setTypeface(urduFont);

        Typeface arabicFont = Typeface.createFromAsset(getAssets(), "fonts/MUHAMMADI QURANIC FONT.ttf");
        bistextview.setTypeface(arabicFont);

}
    private List<Surah> populateSurahsList() {
        return new ArrayList<>();}
    private void setupBismillahTextView() {
        bistextview.setVisibility(View.VISIBLE);
    }
    private void displaySurah(int surahNumber) {
        // Get the Surah object based on surahNumber
        if (surahNumber == 9) { // Example: Surah number 9 (Surah At-Tawbah)
            bistextview.setVisibility(View.GONE);
            bistranslation.setVisibility(View.GONE);
        } else {
            bistextview.setVisibility(View.VISIBLE);
        }
    }
    private void fetchSurahData(String language, int surahId) {
        ApiInterface apiInterface = ApiClientDetail_Quran.getRetrofitInstances().create(ApiInterface.class);
        Call<SurahDetailResponse> call = apiInterface.getSurahDetails(language, surahId);

        call.enqueue(new Callback<SurahDetailResponse>() {
            @Override
            public void onResponse(Call<SurahDetailResponse> call, Response<SurahDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    surahDetails = response.body().getList();
                    if (no == 1 && !surahDetails.isEmpty()) {
                        // Remove the first ayah (Bismillah) from Surah Al-Fatiha
                        surahDetails.remove(0);
                        // Adjust the verse numbers
                        for (int i = 0; i < surahDetails.size(); i++) {
                            surahDetails.get(i).setAya(Integer.parseInt(String.valueOf(i + 1)));
                        }
                    }
                    surahDetailAdapter.updateSurahDetails(surahDetails);
                } else {
                    Log.e("completeSurah", "Response unsuccessful");
                    Toast.makeText(completeSurah.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SurahDetailResponse> call, Throwable t) {
                Log.e("completeSurah", "Request failed", t);
                Toast.makeText(completeSurah.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
