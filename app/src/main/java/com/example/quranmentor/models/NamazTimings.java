package com.example.quranmentor.models;

public class NamazTimings {

    private String fajr;
    private String dhaur;
    private String asr;
    private String maghrib;
    private String isha;
    private String date;



    public NamazTimings(){}
    public NamazTimings(String fajr, String dhaur, String asr, String maghrib, String isha , String date) {
        this.fajr = fajr;
        this.dhaur = dhaur;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
        this.date = date;
    }
    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getDhaur() {
        return dhaur;
    }

    public void setDhaur(String dhaur) {
        this.dhaur = dhaur;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
