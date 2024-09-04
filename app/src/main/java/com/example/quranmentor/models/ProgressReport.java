package com.example.quranmentor.models;

public class ProgressReport {
    private int pronunciation;
    private int fluency;
    private int memorization;
    private int tajweed;
    private int essentialDuas;
    private String studentName;
    private String teacherName;
    private String studentID;
    private String teacherID;
    private String date;
    private String reportID;


    public ProgressReport() {}

    public ProgressReport(int pronunciation, int fluency, int memorization, int tajweed, int essentialDuas , String studentName
    ,String teacherName , String studentID, String teacherID ,String date , String reportID) {
        this.pronunciation = pronunciation;
        this.fluency = fluency;
        this.memorization = memorization;
        this.tajweed = tajweed;
        this.essentialDuas = essentialDuas;
        this.studentName = studentName;
        this.teacherName = teacherName;
        this.studentID = studentID;
        this.teacherID = teacherID;
        this.date = date;
        this.reportID=reportID;
    }

    // Getters and setters
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public int getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(int pronunciation) {
        this.pronunciation = pronunciation;
    }

    public int getFluency() {
        return fluency;
    }

    public void setFluency(int fluency) {
        this.fluency = fluency;
    }

    public int getMemorization() {
        return memorization;
    }

    public void setMemorization(int memorization) {
        this.memorization = memorization;
    }

    public int getTajweed() {
        return tajweed;
    }

    public void setTajweed(int tajweed) {
        this.tajweed = tajweed;
    }
    public int getEssentialDuas() {
        return essentialDuas;
    }
    public void setEssentialDuas(int essentialDuas) {
        this.essentialDuas = essentialDuas;
    }
}
