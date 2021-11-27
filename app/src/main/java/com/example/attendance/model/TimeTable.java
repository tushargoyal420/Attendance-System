package com.example.attendance.model;

public class TimeTable {
    private String StartTime;
    private String EndTime;
    private String Subject;
    private String Room;
    private String Faculty;
    private String Day;

    public TimeTable(String startTime, String endTime, String subject, String room, String faculty, String day) {
        StartTime = startTime;
        EndTime = endTime;
        Subject = subject;
        Room = room;
        Faculty = faculty;
        Day = day;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public TimeTable() {

    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }
}
