package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendance.model.UserData;
import com.github.pierry.simpletoast.SimpleToast;
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
    private DatabaseReference DataRef, userTypeRef;
    private FirebaseAuth fAuth;
    private String User;
    private TextView tUserName, tSapid, tEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t7_profile);
        tprofile_toolbar = findViewById(R.id.profile_toolbar);
        tprofile_toolbar.setTitle("Profile");
        setSupportActionBar(tprofile_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        User = fAuth.getCurrentUser().getUid();

        tUserName = findViewById(R.id.userName);
        tSapid = findViewById(R.id.sapId);
        tEmailAddress = findViewById(R.id.email);

        tprofileimage = findViewById(R.id.profileimage);
        try {
            String CurrentUser = fAuth.getCurrentUser().getUid();
            userTypeRef = FirebaseDatabase.getInstance().getReference("users");
            userTypeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if ((snapshot.child("faculty").child(CurrentUser)).exists()) {
                            String UserType = "faculty";
                            DataRef = FirebaseDatabase.getInstance().getReference("users").child("faculty").child(CurrentUser);
                            LoadUserData(UserType);
                        }
                        if ((snapshot.child("students").child(CurrentUser)).exists()) {
                            String UserType = "student";
                            DataRef = FirebaseDatabase.getInstance().getReference("users").child("students").child(CurrentUser);
                            LoadUserData(UserType);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            SimpleToast.error(t7_profile.this, String.valueOf(e));
        }

    }

    private void LoadUserData(String UserType) {
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData user = dataSnapshot.getValue(UserData.class);
                    Picasso.get().load(user.getImageUri()).into(tprofileimage)
                    ;
                    tUserName.setText(user.getName());
                    tEmailAddress.setText(user.getEmail());
                    if (UserType.equals("student")) {
                        tSapid.setText(user.getSapId());
                    }else{
                        tSapid.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}