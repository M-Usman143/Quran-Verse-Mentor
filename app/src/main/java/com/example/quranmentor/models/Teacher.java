package com.example.quranmentor.models;
public class Teacher {
    public String qualification;
    public String specilization;
    public String experience;
    public String rates;
    public String description;
    public String gender;
    public String city;
    public String country;


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

    public String getSpecilization() {
        return specilization;
    }

    public void setSpecilization(String specilization) {
        this.specilization = specilization;
    }


    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Teacher() {
    }
    public Teacher(String qualification, String experience, String rates, String description, String gender,
    String city, String country , String specilization) {
        this.qualification = qualification;
        this.experience = experience;
        this.rates = rates;
        this.description = description;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.specilization = specilization;}
}

