package com.example.attendance.model;

public class UserData {
    private String imageUri;
    private String Name;
    private String SapId;
    private String Email;
    private String Branch;

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        this.Branch = branch;
    }

    public UserData(String imageUri, String name, String sapId, String email, String branch) {
        this.imageUri = imageUri;
        this.Name = name;
        this.SapId = sapId;
        this.Email = email;
        this.Branch = branch;
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
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSapId() {
        return SapId;
    }

    public void setSapId(String sapId) {
        this.SapId = sapId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
