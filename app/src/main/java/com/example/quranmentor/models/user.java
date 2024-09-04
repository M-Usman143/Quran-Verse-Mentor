package com.example.quranmentor.models;


public class user {
    private String fullname;
    private String email;
    private String number;
    private String profileImageUrl;
    private String UserID;

    private String passward;
    private String conpassward;

    private String spinneroption;

    public user() {
        // Default constructor required for Firebase
    }

    public user(String fullname, String email, String number,
                String passward, String conpassward, String spinneroption , String userID) {
        this.fullname = fullname;
        this.email = email;
        this.number = number;
        this.passward = passward;
        this.conpassward = conpassward;
        this.spinneroption = spinneroption;
        this.UserID = userID;
    }

    public user(String fullname , String profileImageUrl){
        this.fullname = fullname;
        this.profileImageUrl = profileImageUrl;
}

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }

    public String getConpassward() {
        return conpassward;
    }

    public void setConpassward(String conpassward) {
        this.conpassward = conpassward;
    }

    public String getSpinneroption() {
        return spinneroption;
    }

    public void setSpinneroption(String spinneroption) {
        this.spinneroption = spinneroption;
    }

    public void setProfileImageUrl(String userProfile) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

}
