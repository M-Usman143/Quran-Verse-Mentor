package com.example.quranmentor.models;

public class scheduleClass {
    private String time;
    private String date;
    private String title;
    private String tutorName;
    private String studentName;
    private String description;
    private String teacherID;
    private String studentID;
    private String scheduleID;
    public scheduleClass(){}
    public scheduleClass(String studentID,String studentName,String title ,String description
            ,String date,String time,String tutorName,String teacherID , String scheduleID) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.tutorName = tutorName;
        this.teacherID = teacherID;
        this.scheduleID = scheduleID;
    }
    public String getStudentID() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTutorName() {
        return tutorName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTeacherID() {
        return teacherID;
    }
    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getScheduleID() {
        return scheduleID;
    }
    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }
}

