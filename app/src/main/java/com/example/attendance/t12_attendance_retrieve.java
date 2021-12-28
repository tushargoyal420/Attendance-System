package com.example.attendance;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Adapters.AttendanceAdapter;
import com.example.attendance.model.Model;
import com.example.attendance.model.UserData;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class t12_attendance_retrieve extends AppCompatActivity {
    private Toolbar getAttendance_toolbar;
    private RecyclerView attendanceRecyclerview;
    private FirebaseAuth fAuth;
    private List<Model> atList;
    private AttendanceAdapter attendanceAdapter;
    private DatabaseReference userTypeRef, DataRef;

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
        try {
            String CurrentUser = fAuth.getCurrentUser().getUid();
            userTypeRef = FirebaseDatabase.getInstance().getReference().child("users");
            userTypeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if ((snapshot.child("students").child(CurrentUser)).exists()) {
                            UserData userdata = snapshot.child("students").child(CurrentUser).getValue(UserData.class);
                            String Branch= userdata.getBranch();
                            LoadAttendance(Branch);
                        }else{
                            SimpleToast.error(t12_attendance_retrieve.this, "no student exist");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Log.e("UserTypeError", "Exception", e);
        }

    }

    private void LoadAttendance(String Branch) {
        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child(Branch)
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
                                            Model model = singleUserData.getValue(Model.class);
                                            if (!model.getDone().equals("0")) {
                                                Model singleUser = time.child(CurrentUser).getValue(Model.class);
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