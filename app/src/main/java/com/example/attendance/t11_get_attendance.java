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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.attendance.LoginSystem.t1_splash_screen;
import com.example.attendance.LoginSystem.t2_login_signup_choice;
import com.example.attendance.LoginSystem.t3_login;
import com.example.attendance.LoginSystem.t4_signup;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class t11_get_attendance extends AppCompatActivity {
    private Toolbar getAttendance_toolbar;
    private TextView subjectName, dayname, roomNo, starttime, endtime, faculty, lateText;
    private Button markAttendanceBut;
    private Context context = this;
    private Calendar calendar;
    private SimpleDateFormat singletimeFormat, completeTimeFormat, dayFormat, timestampFormat, dateFormat;
    private String completetime, singletime, addedsingletime, getDay, timestamp, date;
    private LinearLayout classDetailsLinear, noclass;
    private ProgressDialog mprogressDialog;
    private DatabaseReference ref;
    private DatabaseReference DataRef;
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
        dayname = findViewById(R.id.dayname);
        roomNo = findViewById(R.id.roomNo);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        faculty = findViewById(R.id.faculty);
        lateText = findViewById(R.id.lateText);
        classDetailsLinear = findViewById(R.id.classDetailsLinear);
        noclass = findViewById(R.id.noclass);

        markAttendanceBut = findViewById(R.id.markAttendanceBut);
        calendar = Calendar.getInstance();
        timestampFormat = new SimpleDateFormat("hh:mm:ss");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        singletimeFormat = new SimpleDateFormat("hh");
        completeTimeFormat = new SimpleDateFormat("hh:mm");
        dayFormat = new SimpleDateFormat("EEEE");
        timestamp = timestampFormat.format(calendar.getTime());
        date = dateFormat.format(calendar.getTime());
        Log.e("checkingdata", date);
        getDay = dayFormat.format(calendar.getTime());
        noclass.setVisibility(View.VISIBLE);
        singletime = singletimeFormat.format(calendar.getTime());
        completetime = completeTimeFormat.format(calendar.getTime());
        addedsingletime = singletime + ":59";

        LoadClass();

    }


    private void LoadClass() {
        String CurrentUser = fAuth.getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("TimeTable/osos")
                .orderByChild("Date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TimeTable timeTable = snapshot.getValue(TimeTable.class);
                        String time = timeTable.getStartTime();
                        if (time.equals(singletime)) {
                            noclass.setVisibility(View.GONE);
                            classDetailsLinear.setVisibility(View.VISIBLE);
                            subjectName.setText(timeTable.getSubject());
                            dayname.setText(timeTable.getDate());
                            roomNo.setText(timeTable.getRoom());
                            starttime.setText(timeTable.getStartTime());
                            endtime.setText(timeTable.getEndTime());
                            faculty.setText(timeTable.getFaculty());
                            String subName = timeTable.getSubject();
                            String sFaculty = timeTable.getFaculty();
                            String sDate = timeTable.getDate();
                            String sRoomno = timeTable.getRoom();

                            String timeperiod = (timeTable.getStartTime() + ":" + timeTable.getEndTime());
                            ref = FirebaseDatabase.getInstance().getReference().child("Attendance").child("osos")
                                    .child(date).child(subName).child(timeperiod);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if (snapshot.exists()) {
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        TimeTable timeTable = data.getValue(TimeTable.class);
                                        if (data.getKey().equals(CurrentUser)) {
                                            if (timeTable.getDone().equals("0")) {
                                                markAttendanceBut.setVisibility(View.VISIBLE);
                                                markAttendanceBut.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mprogressDialog.setMessage("Please wait...");
                                                        mprogressDialog.show();
                                                        mprogressDialog.setCancelable(false);
                                                        markAttendance(sFaculty, subName, sDate, sRoomno);
                                                    }
                                                });
                                            }
                                            if (timeTable.getDone().equals("Present")) {
                                                lateText.setText("You already get attendance");
                                                lateText.setVisibility(View.VISIBLE);
                                                markAttendanceBut.setVisibility(View.GONE);
                                            }
//                                        if (data.getKey().equals(CurrentUser)){
//                                            markAttendanceBut.setVisibility(View.GONE);
//                                            lateText.setText("You already get attendance");
//                                            lateText.setVisibility(View.VISIBLE);
//                                        } else {
//                                            markAttendanceBut.setVisibility(View.VISIBLE);
//                                        }
                                        }
                                    }
//                                    } else {
//                                        markAttendanceBut.setVisibility(View.VISIBLE);
//                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

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
            }
        });
    }

    private void markAttendance(String sFaculty, String subName, String sDate, String sRoomno) {
        try {
            String CurrentUser = fAuth.getCurrentUser().getUid();
            DataRef = FirebaseDatabase.getInstance().getReference().child("students").child(CurrentUser);
            DataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserData user = dataSnapshot.getValue(UserData.class);
//                        String stuName = user.getName();
//                        String email = user.getEmail();
//                        String sapId = user.getSapId();
//                        String imageUri = user.getImageUri();
                        String ctimeStamp = timestamp;
                        HashMap<String, Object> detail = new HashMap<>();
                        detail.put("UserId", CurrentUser);
                        detail.put("TimeStamp", ctimeStamp);
                        detail.put("Done", "Present");
                        detail.put("Faculty", sFaculty);
                        detail.put("Date", sDate);
                        detail.put("Room", sRoomno);
                        detail.put("Subject", subName);
//                        detail.put("SapId", sapId);
//                        detail.put("ImageUri", imageUri);
//                        detail.put("StudentName", stuName);
//                        detail.put("Email", email);
                        ref.child(CurrentUser).updateChildren(detail);
//                                put(detail);
                        markAttendanceBut.setVisibility(View.GONE);
                        mprogressDialog.hide();
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(t11_get_attendance.this, "Database Error!.", Toast.LENGTH_SHORT).show();
                    mprogressDialog.hide();
                }
            });

        } catch (RuntimeException err) {
            Log.e("error", String.valueOf(err));
        }
    }
}