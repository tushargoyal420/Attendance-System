package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendance.LoginSystem.t3_login;
import com.example.attendance.model.UserData;
import com.github.pierry.simpletoast.SimpleToast;
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
    private DatabaseReference studentRoot, facultyRoot, findFacultyName;
    private DatabaseReference attendance;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t10_upload_timetable);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        upload_time_table_toolbar.setTitle("Time Table");
        setSupportActionBar(upload_time_table_toolbar);
        upload_time_table_toolbar = findViewById(R.id.upload_time_table_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String FacultyName = getIntent().getStringExtra("FacultyName");
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
        String facultyS = faculty.getText().toString().trim();
        findFacultyName = FirebaseDatabase.getInstance().getReference("users").child("faculty");
        findFacultyName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot particularFaculty: snapshot.getChildren()) {
                        UserData facultyData = particularFaculty.getValue(UserData.class);
                        String facultyID = particularFaculty.getKey();
                        if (facultyS.equals(facultyData.getName())) {
                            flag = true;
                            uploadTimeTable(facultyS, facultyID);
                            break;
                        }
                    }if(!flag){
                        faculty.setError("Enter correct Faculty");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadTimeTable(String facultyS, String facultyID) {
        try {
            String dateS = date.getText().toString().trim();
            String subS = sub.getText().toString().trim();
            String starttimeS = starttime.getText().toString().trim();
            String endtimeS = endtime.getText().toString().trim();
            String roomS = room.getText().toString().trim();
            String branchS = branch.getText().toString().trim();
            String keyValue = dateS + "--" + starttimeS + "-" + endtimeS;

            HashMap<String, String> studentTimeTable = new HashMap<>();
            studentTimeTable.put("Date", dateS);
            studentTimeTable.put("Subject", subS);
            studentTimeTable.put("Room", roomS);
            studentTimeTable.put("StartTime", starttimeS);
            studentTimeTable.put("EndTime", endtimeS);
            studentTimeTable.put("Faculty", facultyS);

            HashMap<String, String> facultyTimeTable = new HashMap<>();
            facultyTimeTable.put("Date", dateS);
            facultyTimeTable.put("Faculty", facultyS);
            facultyTimeTable.put("Subject", subS);
            facultyTimeTable.put("Room", roomS);
            facultyTimeTable.put("StartTime", starttimeS);
            facultyTimeTable.put("EndTime", endtimeS);
            facultyTimeTable.put("Branch", branchS);
            studentRoot = FirebaseDatabase.getInstance().getReference("TimeTable").child("students").child(branchS);
            studentRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if((snapshot.child(keyValue)).exists()){
                        SimpleToast.error(t10_upload_timetable.this, "There is already a class of the Batch");
                    }
                    else{
                        facultyRoot = FirebaseDatabase.getInstance().getReference("TimeTable").child("faculty").child(facultyID);
                        facultyRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if((snapshot.child(keyValue)).exists()){
                                    SimpleToast.error(t10_upload_timetable.this, "There is another class of the Faculty");
                                }
                                else{
                                    Query query = FirebaseDatabase.getInstance().getReference("studentlist").child(branchS);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String ClassTime = starttimeS+":"+endtimeS;
                                                studentRoot.child(keyValue).setValue(studentTimeTable);
                                                facultyRoot.child(keyValue).setValue(facultyTimeTable);
                                                attendance = FirebaseDatabase.getInstance().getReference("Attendance").child(branchS).child(dateS).child(subS).child(ClassTime);
                                                attendance.setValue(snapshot.getValue());
                                                finish();
                                            }else{
                                                SimpleToast.error(t10_upload_timetable.this, "There is no Student in this batch");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception e) {
            SimpleToast.error(t10_upload_timetable.this, "Error while Uploading TimeTable");
        }
    }
}