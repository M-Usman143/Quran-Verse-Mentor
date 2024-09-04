package com.example.quranmentor.models;

public class Give_Feedback {
    private String teacherId;
    private String teacherName;
    private String studentId;
    private String studentName;
    private float rating;
    private String comment;
    private String feedbackID;
    private long timestamp;

    public Give_Feedback(){}



    public Give_Feedback(String teacherId , String teacherName , String studentId , String studentName,
                         float rating,String comment, String feedbackID , long timestamp ){
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.studentId=studentId;
        this.studentName=studentName;
        this.rating = rating;
        this.comment = comment;
        this.feedbackID = feedbackID;
        this.timestamp  = timestamp;
    }

    // Getters and setters


    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(String feedbackID) {
        this.feedbackID = feedbackID;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}

