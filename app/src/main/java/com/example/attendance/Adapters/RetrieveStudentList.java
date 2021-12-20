package com.example.attendance.Adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attendance.R;

public class RetrieveStudentList extends RecyclerView.ViewHolder {
    public TextView stuname, bool, sapid;
    public CardView card;

    public RetrieveStudentList(@NonNull View itemView) {
        super(itemView);
        stuname = itemView.findViewById(R.id.studentName);
        sapid = itemView.findViewById(R.id.sapId);
        bool = itemView.findViewById(R.id.bool);

        card = itemView.findViewById(R.id.card);
    }
}
