package com.example.attendance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.example.attendance.Adapters.RetreiveTimeTable;
import com.example.attendance.model.TimeTable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class t9_time_table extends AppCompatActivity {
    private Toolbar ttime_table_toolbar;
    private RecyclerView timetableRecyclerview;

    private FirebaseRecyclerAdapter<TimeTable, RetreiveTimeTable> adapter2;
    private FirebaseRecyclerOptions<TimeTable> options;
    private DatabaseReference PostRef = FirebaseDatabase.getInstance().getReference().child("oss").child("monday");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t9_time_table);

        ttime_table_toolbar = findViewById(R.id.setting_toolbar);
        ttime_table_toolbar.setTitle("Time Table");
        setSupportActionBar(ttime_table_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.e("day" , "working");

        timetableRecyclerview = findViewById(R.id.timetableRecyclerview);
        timetableRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        LoadTimeTable();
    }

    private void LoadTimeTable() {
        Log.e("day" , "working2");

        options = new FirebaseRecyclerOptions.Builder<TimeTable>().setQuery(PostRef, TimeTable.class).build();
        adapter2 = new FirebaseRecyclerAdapter<TimeTable, RetreiveTimeTable>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RetreiveTimeTable holder, int position, @NonNull TimeTable timeTable) {
                Log.e("day" , timeTable.getDay());
                Log.e("day" , timeTable.getEndTime());
                Log.e("day" , timeTable.getStartTime());
                Log.e("day" , timeTable.getSubject());



                holder.day.setText(timeTable.getDay());
                holder.subject.setText(timeTable.getSubject());
                holder.startTime.setText(timeTable.getStartTime());
                holder.endTime.setText(timeTable.getEndTime());
                holder.faculty.setText(timeTable.getFaculty());
                holder.roomNo.setText(timeTable.getRoom());

//                holder.card.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(t9_time_table.this, t9_ParticularItemsDetails.class);
//                        intent.putExtra("ItemKey",getRef(position).getKey());
//                        intent.putExtra("Item",lost);
//                        startActivity(intent);
//                    }
//                });
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
