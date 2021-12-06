package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Adapters.RetreiveTimeTable;
import com.example.attendance.model.TimeTable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class t9_time_table extends AppCompatActivity {
    private Toolbar ttime_table_toolbar;
    private RecyclerView timetableRecyclerview;
    private FirebaseRecyclerAdapter<TimeTable, RetreiveTimeTable> adapter2;
    private FirebaseRecyclerOptions<TimeTable> options;
    private DatabaseReference PostRef = FirebaseDatabase.getInstance().getReference().child("TimeTable").child("osos");

    private Button uploadTimeTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t9_time_table);
        ttime_table_toolbar = findViewById(R.id.time_table_toolbar);
        ttime_table_toolbar.setTitle("Time Table");
        setSupportActionBar(ttime_table_toolbar);
        uploadTimeTable = findViewById(R.id.uploadTimeTable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timetableRecyclerview = findViewById(R.id.timetableRecyclerview);
        timetableRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LoadTimeTable();

        uploadTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(t9_time_table.this , t10_upload_timetable.class));
            }
        });
    }
    private void LoadTimeTable() {

        options = new FirebaseRecyclerOptions.Builder<TimeTable>().setQuery(PostRef, TimeTable.class).build();

        adapter2 = new FirebaseRecyclerAdapter<TimeTable, RetreiveTimeTable>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RetreiveTimeTable holder, int position, @NonNull TimeTable timeTable) {

                holder.day.setText(timeTable.getDate());
                holder.subject.setText(timeTable.getSubject());
                holder.startTime.setText(timeTable.getStartTime());
                holder.endTime.setText(timeTable.getEndTime());
                holder.faculty.setText(timeTable.getFaculty());
                holder.roomNo.setText(timeTable.getRoom());
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
    }
}
