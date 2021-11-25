package com.example.attendance.model;

public class UserData {
    private String imageUri;
    private String name;
    private String SapId;
    private String Email;

    public UserData(String imageUri, String name, String sapId, String email) {
        this.imageUri = imageUri;
        this.name = name;
        SapId = sapId;
        Email = email;
    }

    public UserData() {

    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSapId() {
        return SapId;
    }

    public void setSapId(String sapId) {
        SapId = sapId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}