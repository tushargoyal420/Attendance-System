package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class t10_upload_timetable extends AppCompatActivity {
    private Toolbar upload_time_table_toolbar;
    private EditText day, sub, starttime, endtime, room, faculty;
    private Button submit;
    private FirebaseAuth fAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("TimeTable/osos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t10_upload_timetable);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        upload_time_table_toolbar.setTitle("Time Table");
        setSupportActionBar(upload_time_table_toolbar);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        day = findViewById(R.id.day);
        sub = findViewById(R.id.subject);
        starttime = findViewById(R.id.startTime);
        endtime = findViewById(R.id.endTime);
        room = findViewById(R.id.room);
        faculty = findViewById(R.id.faculty);

        submit = findViewById(R.id.submitBut);
        fAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        String days = day.getText().toString().trim();
        String sub = day.getText().toString().trim();
        String starttime = day.getText().toString().trim();
        String endtime = day.getText().toString().trim();
        String room = day.getText().toString().trim();
        String faculty = day.getText().toString().trim();

        HashMap<String, String> timetableMap = new HashMap<>();
        timetableMap.put("Day", days);
        timetableMap.put("Subject", sub);
        timetableMap.put("Room", room);
        timetableMap.put("StartTime", starttime);
        timetableMap.put("EndTime", endtime);
        timetableMap.put("Faculty", faculty);
        root.push().setValue(timetableMap);

    }
}