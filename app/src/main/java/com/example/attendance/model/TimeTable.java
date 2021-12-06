package com.example.attendance.model;

public class TimeTable {
    private String StartTime;
    private String EndTime;
    private String Subject;
    private String Room;
    private String Faculty;
    private String Day;
    private String Date;
    private String TimeStamp;
    private String StudentName;
    private String Attend;
    private String UserId;
    private String Done;

    public String getDone() {
        return Done;
    }

    public void setDone(String done) {
        Done = done;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAttend() {
        return Attend;
    }

    public void setAttend(String attend) {
        Attend = attend;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public TimeTable() {

    }

    public TimeTable(String startTime, String endTime, String subject, String room, String userId,  String faculty, String day, String date, String timestamp, String attend,String done ) {
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Subject = subject;
        this.Room = room;
        this.Faculty = faculty;
        this.Day = day;
        this.Date = date;
        this.TimeStamp = timestamp;
        this.Attend = attend;
        this.UserId= userId;
        this.Done= done;

    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timestamp) {
        TimeStamp = timestamp;
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
