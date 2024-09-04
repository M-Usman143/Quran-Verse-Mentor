package com.example.quranmentor.models;

public class TeacherProfile {
    private String name;
    private String teacherID;
    private String profileImageUri;
    private String price;
    private String gender;
    private String city;
    private String country;
    public String qualification;
    public String specilization;
    public String experience;
    public String description;
    // Constructor
    public TeacherProfile() {}
    public TeacherProfile(String name, String profileImageUri, String price, String gender, String city,
           String country,String qualification ,String specilization,String experience ,String description) {
        this.name = name;
        this.profileImageUri = profileImageUri;
        this.price = price;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.qualification = qualification;
        this.experience = experience;
        this.specilization = specilization;
        this.description = description;}

    public String getQualification() {return qualification;}
    public void setQualification(String qualification) {this.qualification = qualification;}
    public String getSpecilization() {return specilization;}
    public void setSpecilization(String specilization) {this.specilization = specilization;}
    public String getExperience() {return experience;}
    public void setExperience(String experience) {this.experience = experience;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }
    public String getTeacherID() {
        return teacherID;
    }
    public TeacherProfile(String teacherID) {
        this.teacherID=teacherID;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProfileImageUri() {
        return profileImageUri;
    }
    public void setProfileImageUri(String profileImageUri) {this.profileImageUri = profileImageUri;}
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}


