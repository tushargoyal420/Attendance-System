package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.attendance.Adapters.AttendanceAdapter;
import com.example.attendance.Adapters.RetreiveAttendance;
import com.example.attendance.model.TimeTable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private FirebaseRecyclerAdapter<TimeTable, RetreiveAttendance> adapter2;
    private FirebaseRecyclerOptions<TimeTable> options;
    private DatabaseReference PostRef, ref = FirebaseDatabase.getInstance().getReference().child("Attendance").child("osos");
    //            .child("osos").child("01-12-2021").child("OSS for industry").child("02:03");
    private FirebaseAuth fAuth;
    private TextView tdate, tattend, tsubject, troomNo, tjointime, tfaculty;
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
        String CurrentUser = fAuth.getCurrentUser().getUid();

//        options = new FirebaseRecyclerOptions.Builder<TimeTable>().setQuery(PostRef, TimeTable.class).build();
//        adapter2 = new FirebaseRecyclerAdapter<TimeTable, RetreiveAttendance>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull RetreiveAttendance holder, int position, @NonNull TimeTable timeTable) {
////                holder.date.setText(timeTable.getDay());
//                holder.subject.setText(timeTable.getSubject());
//                holder.jointime.setText(timeTable.getStartTime());
//                holder.faculty.setText(timeTable.getFaculty());
//                holder.roomNo.setText(timeTable.getRoom());
//                holder.jointime.setText(timeTable.getTimeStamp());
//            }
//
//            @NonNull
//            @Override
//            public RetreiveAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_class_card, parent, false);
//                return new RetreiveAttendance(view);
//            }
//        };
//        adapter2.startListening();
//        attendanceRecyclerview.setAdapter(adapter2);

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
                                    for(DataSnapshot singleUserData: time.getChildren()){
                                        TimeTable timeTable = singleUserData.getValue(TimeTable.class);
                                        if(timeTable.getDone().equals("0")){
                                            Log.e("classNotexist","notcomplete");
                                        }
                                        else{
                                            TimeTable singleUser = time.child(CurrentUser).getValue(TimeTable.class);
                                            atList.add(singleUser);
                                            attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this,atList);
                                            attendanceRecyclerview.setAdapter(attendanceAdapter);
                                        }
                                    }

                                }
//                                if (time.child(CurrentUser).exists()) {
//                                    TimeTable particularTT = time.child(CurrentUser).getValue((TimeTable.class));
//                                    String attended = "Present";
//                                    atList.add(particularTT);
//                                    attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this, atList, attended);
//                                    break;
//                                }
//                                if (!time.child(CurrentUser).exists()) {
//                                    for (DataSnapshot notPresent : time.getChildren()) {
//                                        String notPresentKey = notPresent.getKey();
//                                        TimeTable all = notPresent.getValue(TimeTable.class);
//                                        String attended = "Absent";
//                                        atList.add(all);
//                                        attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this, atList, attended);
//                                        break;
//                                    }
//                                }

//                                for (DataSnapshot particular :time.getChildren()){
//                            }

//                                    _tmp = particular.getValue((TimeTable.class));
//                                    String particularT = particular.getKey();
//                                    if (particularT.equals(CurrentUser)) {
//                                        attended = "Present";
//                                        atList.add(_tmp);
//                                        attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this, atList, attended);
//                                        attendanceRecyclerview.setAdapter(attendanceAdapter);
//                                        break;
//                                    }else{
//                                        attended = "Absent";
//                                        atList.add(_tmp);
//                                        attendanceAdapter = new AttendanceAdapter(t12_attendance_retrieve.this, atList, attended);
//                                        attendanceRecyclerview.setAdapter(attendanceAdapter);
//                                    }
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