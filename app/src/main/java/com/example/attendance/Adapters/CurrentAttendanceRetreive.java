package com.example.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.R;
import com.example.attendance.model.TimeTable;

import java.util.List;

public class CurrentAttendanceRetreive extends RecyclerView.Adapter<CurrentAttendanceRetreive.ViewHolder> {
    private Context mContext;
    private List<TimeTable> atList;
    private String type;
    public CurrentAttendanceRetreive(Context mContext, List<TimeTable> atList, String type) {
        this.atList = atList;
        this.mContext = mContext;
        this.type = type;
    }
    @NonNull
    @Override
    public CurrentAttendanceRetreive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stu_attendance_card_for_faculty, parent, false);
        return new CurrentAttendanceRetreive.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentAttendanceRetreive.ViewHolder holder, int position) {
        if(type.equals("Present")){
            TimeTable timeTable = atList.get(position);
            holder.stuname.setText(timeTable.getName());
            holder.sapid.setText(timeTable.getSapId());
            holder.bool.setText(timeTable.getTimeStamp());
        }
        if(type.equals("Absent")){
            TimeTable timeTable = atList.get(position);
            holder.stuname.setText(timeTable.getName());
            holder.sapid.setText(timeTable.getSapId());
            holder.bool.setText("Absent");
        }
    }

    @Override
    public int getItemCount() {
        return atList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stuname, bool, sapid;
        public CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stuname = itemView.findViewById(R.id.studentName);
            sapid = itemView.findViewById(R.id.showsapId);
            bool = itemView.findViewById(R.id.bool);
            card = itemView.findViewById(R.id.card);
        }
    }
}
