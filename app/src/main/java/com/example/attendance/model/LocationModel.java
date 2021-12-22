package com.example.attendance.model;

public class LocationModel {
    String FirstLatitude;
    String FirstLongitude;
    String SecondLatitude;
    String SecondLongitude;
    String ThirdLatitude;
    String ThirdLongitude;
    String FourthLatitude;
    String FourthLongitude;

    public LocationModel() {
    }

    public LocationModel(String firstLatitude, String firstLongitude, String secondLatitude, String secondLongitude, String thirdLatitude, String thirdLongitude, String fourthLatitude, String fourthLongitude) {
        FirstLatitude = firstLatitude;
        FirstLongitude = firstLongitude;
        SecondLatitude = secondLatitude;
        SecondLongitude = secondLongitude;
        ThirdLatitude = thirdLatitude;
        ThirdLongitude = thirdLongitude;
        FourthLatitude = fourthLatitude;
        FourthLongitude = fourthLongitude;
    }

    public String getFirstLatitude() {
        return FirstLatitude;
    }

    public void setFirstLatitude(String firstLatitude) {
        FirstLatitude = firstLatitude;
    }

    public String getFirstLongitude() {
        return FirstLongitude;
    }

    public void setFirstLongitude(String firstLongitude) {
        FirstLongitude = firstLongitude;
    }

    public String getSecondLatitude() {
        return SecondLatitude;
    }

    public void setSecondLatitude(String secondLatitude) {
        SecondLatitude = secondLatitude;
    }

    public String getSecondLongitude() {
        return SecondLongitude;
    }

    public void setSecondLongitude(String secondLongitude) {
        SecondLongitude = secondLongitude;
    }

    public String getThirdLatitude() {
        return ThirdLatitude;
    }

    public void setThirdLatitude(String thirdLatitude) {
        ThirdLatitude = thirdLatitude;
    }

    public String getThirdLongitude() {
        return ThirdLongitude;
    }

    public void setThirdLongitude(String thirdLongitude) {
        ThirdLongitude = thirdLongitude;
    }

    public String getFourthLatitude() {
        return FourthLatitude;
    }

    public void setFourthLatitude(String fourthLatitude) {
        FourthLatitude = fourthLatitude;
    }

    public String getFourthLongitude() {
        return FourthLongitude;
    }

    public void setFourthLongitude(String fourthLongitude) {
        FourthLongitude = fourthLongitude;
    }
}
