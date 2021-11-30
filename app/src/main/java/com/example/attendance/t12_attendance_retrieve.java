package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class t12_attendance_retrieve extends AppCompatActivity {
    private Toolbar getAttendance_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t12_attendance_retrieve);
        getAttendance_toolbar = findViewById(R.id.attendanceRetrieve);
        getAttendance_toolbar.setTitle("Your Attendance");
        setSupportActionBar(getAttendance_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}