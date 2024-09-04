package com.example.quranmentor.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientDetail_Quran {
    private static final String BASE_URL = "https://quranenc.com/api/v1/translation/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstances() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();}
        return retrofit;
    }
}