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
import com.example.attendance.model.Model;

import java.util.List;

public class CurrentAttendanceRetreive extends RecyclerView.Adapter<CurrentAttendanceRetreive.ViewHolder> {
    private Context mContext;
    private List<Model> atList;
    private String type;
    public CurrentAttendanceRetreive(Context mContext, List<Model> atList, String type) {
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
            Model model = atList.get(position);
            holder.stuname.setText(model.getName());
            holder.sapid.setText(model.getSapId());
            holder.bool.setText(model.getTimeStamp());
        }
        if(type.equals("Absent")){
            Model model = atList.get(position);
            holder.stuname.setText(model.getName());
            holder.sapid.setText(model.getSapId());
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
