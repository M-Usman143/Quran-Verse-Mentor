package com.example.quranmentor.network;

import com.example.quranmentor.response.QuranResponse;
import com.example.quranmentor.response.SurahDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("surah")
    Call<QuranResponse> getSurahs();

        @GET("sura/{language}/{id}")
        Call<SurahDetailResponse> getSurahDetails(@Path("language") String language, @Path("id") int surahId);


}


