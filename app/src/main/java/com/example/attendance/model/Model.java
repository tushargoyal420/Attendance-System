package com.example.attendance.model;

public class Model {
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
    private String Branch;
    private String SapId;
    private String Name;
    private String LateTime;

    public String getLateTime() {
        return LateTime;
    }

    public void setLateTime(String lateTime) {
        LateTime = lateTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSapId() {
        return SapId;
    }

    public void setSapId(String sapId) {
        SapId = sapId;
    }

    public String getBranch() {
        return Branch;
    }

    public Model(String startTime,String lateTime, String name, String sapId , String endTime, String subject, String room, String faculty, String day, String date, String timeStamp, String studentName, String attend, String userId, String done, String branch) {
        StartTime = startTime;
        Name = name;
        LateTime = lateTime;
        SapId= sapId;
        EndTime = endTime;
        Subject = subject;
        Room = room;
        Faculty = faculty;
        Day = day;
        Date = date;
        TimeStamp = timeStamp;
        StudentName = studentName;
        Attend = attend;
        UserId = userId;
        Done = done;
        Branch = branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

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

    public Model() {

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
