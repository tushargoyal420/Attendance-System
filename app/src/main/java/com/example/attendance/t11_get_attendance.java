package com.example.attendance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.attendance.model.TimeTable;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class t11_get_attendance extends AppCompatActivity {
    private Toolbar getAttendance_toolbar;
    private TextView subjectName, dateName, roomNo, starttime, endtime, faculty, lateText, noclassText;
    private Button markAttendanceBut, mcreateClassButton, mleftSmallBut;
    private Context context = this;
    private Calendar calendar;
    private SimpleDateFormat singletimeFormat, completeTimeFormat, dayFormat, timestampFormat, dateFormat;
    private String completetime, singletime, addedsingletime, getDay, timestamp, date;
    private LinearLayout classDetailsLinear, noclass, tforFacultyLinear;
    private ProgressDialog mprogressDialog;
    private DatabaseReference ref, attendance;

    FirebaseAuth fAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final int SPLASH_TIME_OUT = 2000;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t11_get_attendance);
        getAttendance_toolbar = findViewById(R.id.getAttendance_toolbar);
        getAttendance_toolbar.setTitle("Get Attendance");
        setSupportActionBar(getAttendance_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();
        mprogressDialog = new ProgressDialog(this);
        subjectName = findViewById(R.id.subjectName);
        dateName = findViewById(R.id.dayname);
        roomNo = findViewById(R.id.roomNo);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        faculty = findViewById(R.id.faculty);
        lateText = findViewById(R.id.lateText);
        classDetailsLinear = findViewById(R.id.classDetailsLinear);
        noclass = findViewById(R.id.noclass);
        noclassText = findViewById(R.id.noclassText);
        mcreateClassButton = findViewById(R.id.createClassButton);
        mleftSmallBut = findViewById(R.id.leftSmallBut);
        tforFacultyLinear = findViewById(R.id.forFacultyLinear);
        mleftSmallBut.setText("Check Attendance");

        markAttendanceBut = findViewById(R.id.markAttendanceBut);
        calendar = Calendar.getInstance();
        timestampFormat = new SimpleDateFormat("hh:mm:ss");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        singletimeFormat = new SimpleDateFormat("hh");
        completeTimeFormat = new SimpleDateFormat("hh:mm");
        dayFormat = new SimpleDateFormat("EEEE");
        timestamp = timestampFormat.format(calendar.getTime());
        date = dateFormat.format(calendar.getTime());
        getDay = dayFormat.format(calendar.getTime());
        noclass.setVisibility(View.VISIBLE);
        singletime = singletimeFormat.format(calendar.getTime());
        completetime = completeTimeFormat.format(calendar.getTime());
        addedsingletime = singletime + ":59";

        try {
            String UserType = getIntent().getStringExtra("UserType");
            String UserBranchOrName = getIntent().getStringExtra("UserBranchOrName");
            String CurrentUser = fAuth.getCurrentUser().getUid();

            if (UserType.equals("faculty")) {
                Query query = FirebaseDatabase.getInstance().getReference().child("TimeTable/faculty").child(UserBranchOrName).orderByChild("Date").equalTo(date);
                String type = "faculty";
                LoadClass(query, CurrentUser, type, UserBranchOrName);
            }
            if (UserType.equals("student")) {
                Query query = FirebaseDatabase.getInstance().getReference().child("TimeTable/students").child(UserBranchOrName).orderByChild("Date").equalTo(date);
                String type = "student";
                LoadClass(query, CurrentUser, type, UserBranchOrName);
            }
        } catch (Exception e) {
            noclassText.setText("Error to Load class");
            noclass.setVisibility(View.VISIBLE);
        }
    }

    private void LoadClass(Query query, String CurrentUser, String type, String UserBranchOrName) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TimeTable timeTable = snapshot.getValue(TimeTable.class);
                        String time = timeTable.getStartTime();
                        if (time.equals(singletime)) {
                            String starttimeT = timeTable.getStartTime();
                            String endtimeT = timeTable.getEndTime();
                            String branchS = timeTable.getBranch();
                            String subName = timeTable.getSubject();
                            String sFaculty = timeTable.getFaculty();
                            String sDate = timeTable.getDate();
                            String sRoomno = timeTable.getRoom();
                            String ctimeStamp = timestamp;
                            String ClassTime = starttimeT + ":" + endtimeT;

                            noclass.setVisibility(View.GONE);
                            classDetailsLinear.setVisibility(View.VISIBLE);
                            subjectName.setText(subName);
                            dateName.setText(sDate);
                            roomNo.setText(sRoomno);
                            starttime.setText(starttimeT);
                            endtime.setText(endtimeT);

                            HashMap<String, Object> fromFaculty = new HashMap<>();
                            fromFaculty.put("Done", "Absent");
                            fromFaculty.put("Faculty", sFaculty);
                            fromFaculty.put("Date", sDate);
                            fromFaculty.put("Room", sRoomno);
                            fromFaculty.put("Subject", subName);

                            HashMap<String, Object> studentDetail = new HashMap<>();
                            studentDetail.put("UserId", CurrentUser);
                            studentDetail.put("TimeStamp", ctimeStamp);
                            studentDetail.put("Done", "Present");
                            studentDetail.put("Faculty", sFaculty);
                            studentDetail.put("Date", sDate);
                            studentDetail.put("Room", sRoomno);
                            studentDetail.put("Subject", subName);
                            String timeperiod = (timeTable.getStartTime() + ":" + timeTable.getEndTime());

                            if (type.equals("faculty")) {
                                faculty.setText(branchS);
                                tforFacultyLinear.setVisibility(View.VISIBLE);
                                mcreateClassButton.setText("Create Class");
                                mcreateClassButton.setVisibility(View.VISIBLE);

                                mleftSmallBut.setVisibility(View.VISIBLE);
                                mleftSmallBut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(t11_get_attendance.this, t13_check_attendance_activity.class);
                                        intent.putExtra("Branch", branchS);
                                        intent.putExtra("Date", sDate);
                                        intent.putExtra("SubjectName", subName);
                                        intent.putExtra("Time", ClassTime);
                                        intent.putExtra("Room", sRoomno);
                                        intent.putExtra("Faculty", sFaculty);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                mcreateClassButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mprogressDialog.setMessage("Please wait...");
                                        mprogressDialog.show();
                                        mprogressDialog.setCancelable(false);
                                        attendance = FirebaseDatabase.getInstance().getReference().child("Attendance").child(branchS).child(sDate).child(subName).child(ClassTime);
                                        facultyCrateClass(branchS, fromFaculty);
                                    }
                                });
                            }
                            if (type.equals("student")) {
                                faculty.setText(timeTable.getFaculty());
                                ref = FirebaseDatabase.getInstance().getReference().child("Attendance").child(UserBranchOrName).child(date).child(subName).child(timeperiod);
                                if (ref != null) {
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                TimeTable timeTable = data.getValue(TimeTable.class);
                                                if (data.getKey().equals(CurrentUser)) {
                                                    if (timeTable.getDone().equals("0") || timeTable.getDone().equals("Absent")) {
                                                        markAttendanceBut.setVisibility(View.VISIBLE);
                                                        markAttendanceBut.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mprogressDialog.setMessage("Please wait...");
                                                                mprogressDialog.show();
                                                                mprogressDialog.setCancelable(false);
                                                                markAttendance(studentDetail);
                                                            }
                                                        });
                                                    }
                                                    if (timeTable.getDone().equals("Present")) {
                                                        lateText.setText("You already get attendance");
                                                        lateText.setVisibility(View.VISIBLE);
                                                        markAttendanceBut.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            SimpleToast.error(t11_get_attendance.this, "Error to getiting Student List");
                                            mprogressDialog.dismiss();
                                        }
                                    });
                                }
                            }

//                            ref = FirebaseDatabase.getInstance().getReference().child("Attendance").child("osos").child(date).child(subName).child(timeperiod);
//                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    for (DataSnapshot data : snapshot.getChildren()) {
//                                        TimeTable timeTable = data.getValue(TimeTable.class);
//                                        if (data.getKey().equals(CurrentUser)) {
//                                            if (timeTable.getDone().equals("0")) {
//                                                markAttendanceBut.setVisibility(View.VISIBLE);
//                                                markAttendanceBut.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        mprogressDialog.setMessage("Please wait...");
//                                                        mprogressDialog.show();
//                                                        mprogressDialog.setCancelable(false);
//                                                        markAttendance(sFaculty, subName, sDate, sRoomno);
//                                                    }
//                                                });
//                                            }
//                                            if (timeTable.getDone().equals("Present")) {
//                                                lateText.setText("You already get attendance");
//                                                lateText.setVisibility(View.VISIBLE);
//                                                markAttendanceBut.setVisibility(View.GONE);
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                }
//                            });

                            if (completetime.compareTo(addedsingletime) >= 0) {
                                lateText.setText("You are late");
                                lateText.setVisibility(View.VISIBLE);
                                markAttendanceBut.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    lateText.setVisibility(View.GONE);
                    markAttendanceBut.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                SimpleToast.error(t11_get_attendance.this, "Error to getiting Student List");
                mprogressDialog.dismiss();
            }
        });
    }

    private void facultyCrateClass(String branchS, HashMap map) {
        try {
            Query query = FirebaseDatabase.getInstance().getReference("studentlist").child(branchS);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot sesnapshot) {
                    if (sesnapshot.exists()) {
                        attendance.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot every : snapshot.getChildren()) {
                                    String everyKey = every.getKey();
                                    attendance.child(everyKey).updateChildren(map);
                                    mcreateClassButton.setVisibility(View.GONE);
                                    SimpleToast.ok(t11_get_attendance.this, "Created");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(t11_get_attendance.this, t6_dashboard.class);
                                            mprogressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, SPLASH_TIME_OUT);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                SimpleToast.error(t11_get_attendance.this, "Error to create attendance list");
                                mprogressDialog.dismiss();
                            }
                        });
                    } else {
                        SimpleToast.error(t11_get_attendance.this, "Error to ");
                        mprogressDialog.dismiss();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    SimpleToast.error(t11_get_attendance.this, "Error to getiting Student List");
                    mprogressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.e("Mine", "FacultyCreateClass", e);
        }
    }

    private void markAttendance(HashMap detail) {
        try {
            String CurrentUser = fAuth.getCurrentUser().getUid();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ref.child(CurrentUser).updateChildren(detail);
                    markAttendanceBut.setVisibility(View.GONE);
                    mprogressDialog.dismiss();
                    SimpleToast.ok(t11_get_attendance.this, "Marked");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(t11_get_attendance.this, t6_dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    SimpleToast.error(t11_get_attendance.this, "Error to Mark Attendance");
                    mprogressDialog.dismiss();
                }
            });
        } catch (RuntimeException err) {
            mprogressDialog.hide();
            Log.e("error", String.valueOf(err));
        }
    }
}