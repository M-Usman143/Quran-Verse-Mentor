package com.example.quranmentor.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.SurahAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.network.ApiClient;
import com.example.quranmentor.network.ApiInterface;
import com.example.quranmentor.response.QuranResponse;
import com.example.quranmentor.models.Surah;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuranActivity extends AppCompatActivity{
    private SurahAdapter surahAdapter;
    RecyclerView QuranRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        QuranRecyclerview = findViewById(R.id.quranrecyclerView);
        QuranRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        fetchSurahData();
    }
    private void fetchSurahData() {
        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<QuranResponse> call = apiInterface.getSurahs();

        call.enqueue(new Callback<QuranResponse>() {
            @Override
            public void onResponse(Call<QuranResponse> call, Response<QuranResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Surah>surahList = response.body().getSurahs();
                    surahAdapter = new SurahAdapter(surahList , QuranActivity.this);
                    QuranRecyclerview.setAdapter(surahAdapter);
                } else {
                    Log.e("MainActivity", "Response unsuccessful");
                    Toast.makeText(QuranActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<QuranResponse> call, Throwable t) {
                Log.e("MainActivity", "Request failed", t);
                Toast.makeText(QuranActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }

            public void onSuccess() {
            }

            public void onError(Exception e) {
            }
        });
    }

}
