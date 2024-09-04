package com.example.quranmentor.response;

import com.example.quranmentor.models.Surah;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuranResponse {
    @SerializedName("data")
    private List<Surah> surahs;

    public List<Surah> getSurahs() {
        return surahs;
    }
}
