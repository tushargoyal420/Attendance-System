package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class t10_upload_timetable extends AppCompatActivity {
    private Toolbar upload_time_table_toolbar;
    private EditText date, sub, starttime, endtime, room, faculty, branch;
    private Button submit;
    private FirebaseAuth fAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("TimeTable/osos");
    private DatabaseReference attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t10_upload_timetable);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        upload_time_table_toolbar.setTitle("Time Table");
        setSupportActionBar(upload_time_table_toolbar);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.date);
        sub = findViewById(R.id.subject);
        starttime = findViewById(R.id.startTime);
        endtime = findViewById(R.id.endTime);
        room = findViewById(R.id.room);
        branch = findViewById(R.id.branch);
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
        String dateS = date.getText().toString().trim();
        String subS = sub.getText().toString().trim();
        String starttimeS = starttime.getText().toString().trim();
        String endtimeS = endtime.getText().toString().trim();
        String roomS = room.getText().toString().trim();
        String facultyS = faculty.getText().toString().trim();
        String branchS = branch.getText().toString().trim();

        HashMap<String, String> timetableMap = new HashMap<>();
        timetableMap.put("Date", dateS);
        timetableMap.put("Subject", subS);
        timetableMap.put("Room", roomS);
        timetableMap.put("StartTime", starttimeS);
        timetableMap.put("EndTime", endtimeS);
        timetableMap.put("Faculty", facultyS);
        root.push().setValue(timetableMap);
        Query query = FirebaseDatabase.getInstance().getReference().child("studentlist").child(branchS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ClassTime= starttimeS+":"+endtimeS;
                    attendance = FirebaseDatabase.getInstance().getReference().child("Attendance").child(branchS).child(dateS).child(subS).child(ClassTime);
                    attendance.setValue(snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}