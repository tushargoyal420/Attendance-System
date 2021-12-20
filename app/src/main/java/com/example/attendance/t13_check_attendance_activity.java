package com.example.attendance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Adapters.CurrentAttendanceRetreive;
import com.example.attendance.model.TimeTable;
import com.example.attendance.model.UserData;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class t13_check_attendance_activity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView tpresentList, tabsentList;
    private TextView tNoPresentStu, tNoAbsentStu, tstudentName;
    private FirebaseAuth fAuth;
    private List<TimeTable> presentList, absentList;
    private CurrentAttendanceRetreive currentAttendanceRetreive;
    private Button taddAttendance, tadd, tcheck, tremove;
    private EditText tEntersapid;
    private LinearLayout taddAttendanceLinear, tclassCreatedLinear, tclassCreatedNotLinear;
    private DatabaseReference findStudentId, uploadAttendance;
    boolean flag = false;
    private SimpleDateFormat timestampFormat;
    private String timestamp;
    private Calendar calendar;
    private ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t13_check_attendance);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Check Attendance");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timestampFormat = new SimpleDateFormat("hh:mm:ss");

        calendar = Calendar.getInstance();
        tclassCreatedLinear = findViewById(R.id.allLayout);
        tclassCreatedNotLinear = findViewById(R.id.classNotCreated);

        timestamp = timestampFormat.format(calendar.getTime());
        mprogressDialog = new ProgressDialog(this);

        tcheck = findViewById(R.id.check);
        tremove = findViewById(R.id.remove);
        tadd = findViewById(R.id.add);
        tstudentName = findViewById(R.id.studentName);

        tNoPresentStu = findViewById(R.id.NoPresentStu);
        tNoAbsentStu = findViewById(R.id.NoAbsentStu);
        tpresentList = findViewById(R.id.presentList);
        tpresentList.setLayoutManager(new LinearLayoutManager(this));
        tabsentList = findViewById(R.id.absentList);
        tabsentList.setLayoutManager(new LinearLayoutManager(this));
        String Branch = getIntent().getStringExtra("Branch");
        String Date = getIntent().getStringExtra("Date");
        String SubjectName = getIntent().getStringExtra("SubjectName");
        String Time = getIntent().getStringExtra("Time");
        String Faculty = getIntent().getStringExtra("Faculty");
        String Room = getIntent().getStringExtra("Room");
        try {
            retrieveList(Branch, Date, SubjectName, Time);
        } catch (Exception e) {
            Log.e("CheckAttendance", "exception", e);
        }

        taddAttendanceLinear = findViewById(R.id.addAttendanceLinear);
        tEntersapid = findViewById(R.id.Entersapid);
        taddAttendance = findViewById(R.id.addAttendance);
        taddAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taddAttendanceLinear.getVisibility() == View.VISIBLE) {
                    taddAttendanceLinear.setVisibility(View.GONE);
                } else {
                    taddAttendanceLinear.setVisibility(View.VISIBLE);
                }
            }
        });
        tcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tEntersapid.getText().toString().trim())) {
                    tEntersapid.setError("Please enter SapId");
                    return;
                } else {
                    mprogressDialog.setMessage("Please wait...");
                    mprogressDialog.show();
                    mprogressDialog.setCancelable(false);
                    CheckStudent(Branch, Date, SubjectName, Time, Faculty, Room);
                }
            }
        });

    }

    private void retrieveList(String Branch, String Date, String SubjectName, String Time) {

        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child(Branch).child(Date).child(SubjectName).child(Time);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    presentList = new ArrayList<>();
                    absentList = new ArrayList<>();
                    for (DataSnapshot particular : dataSnapshot.getChildren()) {
                        TimeTable data = particular.getValue(TimeTable.class);
                        if ((data.getDone()).equals("Present")) {
                            presentList.add(data);
                            currentAttendanceRetreive = new CurrentAttendanceRetreive(t13_check_attendance_activity.this, presentList, "Present");
                            tpresentList.setVisibility(View.VISIBLE);
                            tNoPresentStu.setVisibility(View.GONE);
                            tpresentList.setAdapter(currentAttendanceRetreive);
                        }
                        if ((data.getDone()).equals("Absent")) {
                            absentList.add(data);
                            currentAttendanceRetreive = new CurrentAttendanceRetreive(t13_check_attendance_activity.this, absentList, "Absent");
                            tabsentList.setVisibility(View.VISIBLE);
                            tNoAbsentStu.setVisibility(View.GONE);
                            tabsentList.setAdapter(currentAttendanceRetreive);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                SimpleToast.error(t13_check_attendance_activity.this, "Database Error");
            }
        });
    }

    private void CheckStudent(String Branch, String Date, String SubjectName, String Time, String Faculty, String Room) {
        String Sap = tEntersapid.getText().toString().trim();
        findStudentId = FirebaseDatabase.getInstance().getReference("users").child("students");
        findStudentId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot particularStudent : snapshot.getChildren()) {
                        UserData studentData = particularStudent.getValue(UserData.class);
                        if (Sap.equals(studentData.getSapId())) {
                            flag = true;
                            String UserId = particularStudent.getKey();
                            tcheck.setVisibility(View.GONE);
                            String userName = studentData.getName();
                            String sapid = studentData.getSapId();
                            tstudentName.setText(userName);
                            tstudentName.setVisibility(View.VISIBLE);
                            AddStudent(Branch, Date, SubjectName, Time, UserId, Faculty, Room, userName, sapid);
                            break;
                        }
                    }
                    if (!flag) {
                        tEntersapid.setError("No student find");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SimpleToast.error(t13_check_attendance_activity.this, "Database Error");
            }
        });
    }

    private void AddStudent(String Branch, String Date, String SubjectName, String Time, String UserId, String Faculty, String Room, String userName, String sapid) {
        tadd.setVisibility(View.VISIBLE);
        tremove.setVisibility(View.VISIBLE);
        mprogressDialog.hide();

        HashMap<String, Object> studentDetail = new HashMap<>();
        studentDetail.put("TimeStamp", timestamp);
        studentDetail.put("Date", Date);
        studentDetail.put("Done", "Present");
        studentDetail.put("Faculty", Faculty);
        studentDetail.put("Room", Room);
        studentDetail.put("Subject", SubjectName);

        HashMap<String, Object> absentStudentDetail = new HashMap<>();
        absentStudentDetail.put("Date", Date);
        absentStudentDetail.put("Done", "Absent");
        absentStudentDetail.put("Faculty", Faculty);
        absentStudentDetail.put("Name", userName);
        absentStudentDetail.put("Room", Room);
        absentStudentDetail.put("SapId", sapid);
        absentStudentDetail.put("Subject", SubjectName);

        tadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressDialog.show();
                uploadAttendance = FirebaseDatabase.getInstance().getReference().child("Attendance").child(Branch).child(Date).child(SubjectName).child(Time);
                uploadAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            TimeTable timeTable = data.getValue(TimeTable.class);
                            if (data.getKey().equals(UserId)) {
                                if (timeTable.getDone().equals("0") || timeTable.getDone().equals("Absent")) {
                                    uploadAttendance.child(UserId).updateChildren(studentDetail);
                                    mprogressDialog.dismiss();
                                    finish();
                                }
                                if (timeTable.getDone().equals("Present")) {
                                    mprogressDialog.hide();
                                    SimpleToast.error(t13_check_attendance_activity.this, "Already get attendance");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mprogressDialog.dismiss();
                        SimpleToast.error(t13_check_attendance_activity.this, "Error to add Student");
                    }
                });
            }
        });
        tremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressDialog.show();
                uploadAttendance = FirebaseDatabase.getInstance().getReference().child("Attendance").child(Branch).child(Date).child(SubjectName).child(Time);
                uploadAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            TimeTable timeTable = data.getValue(TimeTable.class);
                            if (data.getKey().equals(UserId)) {
                                if (timeTable.getDone().equals("0") || timeTable.getDone().equals("Absent")) {
                                    SimpleToast.error(t13_check_attendance_activity.this, "Already absent");
                                    mprogressDialog.hide();

                                }
                                if (timeTable.getDone().equals("Present")) {
                                    uploadAttendance.child(UserId).setValue(absentStudentDetail);
                                    finish();
                                    mprogressDialog.dismiss();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        SimpleToast.error(t13_check_attendance_activity.this, "Error to remove student");
                        mprogressDialog.dismiss();
                    }
                });
            }
        });


    }

}