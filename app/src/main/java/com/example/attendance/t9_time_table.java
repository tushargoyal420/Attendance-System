package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Adapters.RetreiveTimeTable;
import com.example.attendance.model.Model;
import com.example.attendance.model.UserData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class t9_time_table extends AppCompatActivity {
    private Toolbar ttime_table_toolbar;
    private RecyclerView timetableRecyclerview;
    private FirebaseRecyclerAdapter<Model, RetreiveTimeTable> adapter2;
    private FirebaseRecyclerOptions<Model> options;
    private DatabaseReference PostRef;
    private FirebaseAuth fAuth;
    private DatabaseReference getBranch, userTypeRef;
    private LinearLayout noclass;
    private Button uploadTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t9_time_table);
        ttime_table_toolbar = findViewById(R.id.time_table_toolbar);
        ttime_table_toolbar.setTitle("Time Table");
        setSupportActionBar(ttime_table_toolbar);

        uploadTimeTable = findViewById(R.id.uploadTimeTable);
        uploadTimeTable.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();
        noclass = findViewById(R.id.noclass);
        timetableRecyclerview = findViewById(R.id.timetableRecyclerview);
        timetableRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        try {
            String CurrentUser = fAuth.getCurrentUser().getUid();
            userTypeRef = FirebaseDatabase.getInstance().getReference("users");
            userTypeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if ((snapshot.child("faculty").child(CurrentUser)).exists()) {
                        LoadFacultyTimeTable(CurrentUser);
                    }
                    if ((snapshot.child("students").child(CurrentUser)).exists()) {
                        UserData data =
                                snapshot.child("students").child(CurrentUser).getValue(UserData.class);
                        String Branch = data.getBranch();
                        LoadTimeTable(Branch);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            SimpleToast.error(t9_time_table.this, String.valueOf(e));
        }
    }

    private void LoadFacultyTimeTable(String Name) {
        PostRef = FirebaseDatabase.getInstance().getReference().child("TimeTable").child("faculty").child(Name);
        if (PostRef != null) {
            options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(PostRef, Model.class).build();
            adapter2 = new FirebaseRecyclerAdapter<Model, RetreiveTimeTable>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RetreiveTimeTable holder, int position, @NonNull Model model) {
                    holder.day.setText(model.getDate());
                    holder.subject.setText(model.getSubject());
                    holder.startTime.setText(model.getStartTime());
                    holder.endTime.setText(model.getEndTime());
                    holder.roomNo.setText(model.getRoom());
                    holder.faculty.setText(model.getBranch());
                }

                @NonNull
                @Override
                public RetreiveTimeTable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetablecard, parent, false);
                    return new RetreiveTimeTable(view);
                }
            };
            adapter2.startListening();
            timetableRecyclerview.setAdapter(adapter2);
        } else {
            noclass.setVisibility(View.VISIBLE);
        }
        uploadTimeTable.setVisibility(View.VISIBLE);
        uploadTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t9_time_table.this, t10_upload_timetable.class);
                intent.putExtra("FacultyName", Name);
                startActivity(intent);
            }
        });

    }

    private void LoadTimeTable(String Branch) {
        PostRef = FirebaseDatabase.getInstance().getReference().child("TimeTable").child("students").child(Branch);
        if (PostRef != null) {
            options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(PostRef, Model.class).build();
            adapter2 = new FirebaseRecyclerAdapter<Model, RetreiveTimeTable>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RetreiveTimeTable holder, int position, @NonNull Model model) {

                    holder.day.setText(model.getDate());
                    holder.subject.setText(model.getSubject());
                    holder.startTime.setText(model.getStartTime());
                    holder.endTime.setText(model.getEndTime());
                    holder.faculty.setText(model.getFaculty());
                    holder.roomNo.setText(model.getRoom());
                }

                @NonNull
                @Override
                public RetreiveTimeTable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetablecard, parent, false);
                    return new RetreiveTimeTable(view);
                }
            };
            adapter2.startListening();
            timetableRecyclerview.setAdapter(adapter2);
        } else {
            noclass.setVisibility(View.VISIBLE);
        }
    }
}
