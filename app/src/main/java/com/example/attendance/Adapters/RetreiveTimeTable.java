package com.example.attendance.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.R;

public class RetreiveTimeTable extends RecyclerView.ViewHolder {
    public TextView day, subject, startTime, endTime, faculty, roomNo;
    public CardView card;

    public RetreiveTimeTable(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.dayname);
        subject = itemView.findViewById(R.id.subjectName);
        startTime = itemView.findViewById(R.id.starttime);
        endTime = itemView.findViewById(R.id.endtime);
        faculty = itemView.findViewById(R.id.faculty);
        roomNo = itemView.findViewById(R.id.roomNo);


        card = itemView.findViewById(R.id.card);
    }
}
