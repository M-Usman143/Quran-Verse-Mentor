package com.example.quranmentor.response;

import com.example.quranmentor.models.SurahDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurahDetailResponse {

@SerializedName("result")
    private List<SurahDetail>list;

    public SurahDetailResponse(List<SurahDetail> list) {
        this.list = list;
    }

    public List<SurahDetail> getList() {
        return list;
    }
}
