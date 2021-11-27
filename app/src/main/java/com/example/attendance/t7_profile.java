package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendance.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class t7_profile extends AppCompatActivity {
    private Toolbar tprofile_toolbar;
    private CircleImageView tprofileimage;
    private DatabaseReference ref, DataRef;
    private FirebaseAuth fAuth;
    private String User;
    private TextView tUserName, tSapid, tEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t7_profile);
        tprofile_toolbar= findViewById(R.id.profile_toolbar);
        tprofile_toolbar.setTitle("Profile");
        setSupportActionBar(tprofile_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        User = fAuth.getCurrentUser().getUid();

        tUserName= findViewById(R.id.userName);
        tSapid= findViewById(R.id.sapId);
        tEmailAddress= findViewById(R.id.email);

        tprofileimage = findViewById(R.id.profileimage);

        LoadUserData();
    }

    private void LoadUserData() {
        String CurrentUser= fAuth.getCurrentUser().getUid();
        DataRef = FirebaseDatabase.getInstance().getReference().child("students").child(CurrentUser);
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData user= dataSnapshot.getValue(UserData.class);
                    Picasso.get().load(user.getImageUri()).into(tprofileimage);
                    tUserName.setText(user.getName());
                    tSapid.setText(user.getSapId());
                    tEmailAddress.setText(user.getEmail());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}