package com.example.attendance.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.R;

public class RetreiveAttendance extends RecyclerView.ViewHolder{

    public TextView date, attend, subject, jointime, faculty, roomNo;
    public CardView card;

    public RetreiveAttendance(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        attend = itemView.findViewById(R.id.attend);
        subject = itemView.findViewById(R.id.subject);
        roomNo = itemView.findViewById(R.id.roomNo);
        jointime = itemView.findViewById(R.id.jointime);
        faculty = itemView.findViewById(R.id.faculty);


        card = itemView.findViewById(R.id.card);
    }
}
