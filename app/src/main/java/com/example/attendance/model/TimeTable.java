package com.example.attendance.model;

public class TimeTable {
    private String StartTime;
    private String EndTime;
    private String Subject;
    private String Room;
    private String Faculty;
    private String Day;

    public TimeTable(String startTime, String endTime, String subject, String room, String faculty, String day) {
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Subject = subject;
        this.Room = room;
        this.Faculty = faculty;
       this.Day = day;
    }
    public TimeTable() {

    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        this.Day = day;
    }


    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        this.StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        this.EndTime = endTime;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        this.Room = room;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        this.Faculty = faculty;
    }
}
