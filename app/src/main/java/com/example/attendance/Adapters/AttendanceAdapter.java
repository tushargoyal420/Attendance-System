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
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AttendanceAdapter  extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private Context mContext;
    private List<Model> atList;
    FirebaseUser fuser;
    public AttendanceAdapter(Context mContext, List<Model> atList) {
        this.atList = atList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attendance_class_card, parent, false);
        return new AttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model attend = atList.get(position);
        holder.date.setText(attend.getDate());
        holder.subject.setText(attend.getSubject());
        holder.roomNo.setText(attend.getRoom());
        holder.jointime.setText(attend.getTimeStamp());
        holder.faculty.setText(attend.getFaculty());
        if(attend.getDone().equals("0")){
            holder.attend.setText("Not Conducted");
        }if(attend.getDone().equals("Present")){
                    holder.attend.setText("Present");
        }if(attend.getDone().equals("Absent")){
            holder.attend.setText("Absent");
        }
//        holder.attend.setText(attend.getAttend());
    }

    @Override
    public int getItemCount() {
        return atList.size();    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, attend, subject, jointime, faculty, roomNo;
        public CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            attend = itemView.findViewById(R.id.attend);
            subject = itemView.findViewById(R.id.subject);
            roomNo = itemView.findViewById(R.id.roomNo);
            jointime = itemView.findViewById(R.id.jointime);
            faculty = itemView.findViewById(R.id.faculty);

        }
    }
}
