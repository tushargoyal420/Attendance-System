package com.example.attendance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Adapters.AttendanceAdapter;
import com.example.attendance.model.TimeTable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class t12_attendance_retrieve extends AppCompatActivity {
    private Toolbar getAttendance_toolbar;
    private RecyclerView attendanceRecyclerview;
    private FirebaseAuth fAuth;
    private List<TimeTable> atList;
    private AttendanceAdapter attendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t12_attendance_retrieve);
        getAttendance_toolbar = findViewById(R.id.attendanceRetrieve);
        getAttendance_toolbar.setTitle("Your Attendance");
        setSupportActionBar(getAttendance_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();

        attendanceRecyclerview = findViewById(R.id.attendanceRecyclerview);
        attendanceRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LoadAttendance();
    }

    private void LoadAttendance() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Attendance/osos")
                .orderByPriority();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String CurrentUser = fAuth.getCurrentUser().getUid();
                if (dataSnapshot.exists()) {
                    atList = new ArrayList<>();
                    for (DataSnapshot date : dataSnapshot.getChildren()) {
                        String dateT = date.getKey();
                        for (DataSnapshot subject : date.getChildren()) {
                            String subjectT = subject.getKey();
                            for (DataSnapshot time : subject.getChildren()) {
                                if (time.child(CurrentUser).exists()) {
                                    for (DataSnapshot singleUserData : time.getChildren()) {
                                        if (singleUserData.getKey().equals(CurrentUser)) {
                                            TimeTable timeTable = singleUserData.getValue(TimeTable.class);
                                            if (!timeTable.getDone().equals("0")) {
                                                TimeTable singleUser = time.child(CurrentUser).getValue(TimeTable.class);
                                                atList.add(singleUser);
                                                attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this, atList);
                                                attendanceRecyclerview.setAdapter(attendanceAdapter);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}