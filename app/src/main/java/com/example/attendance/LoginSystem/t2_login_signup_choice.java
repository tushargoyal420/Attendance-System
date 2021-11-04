package com.example.attendance.LoginSystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.attendance.R;
import com.example.attendance.t6_dashboard;
import com.google.firebase.auth.FirebaseAuth;

public class t2_login_signup_choice extends AppCompatActivity {
    private Button tsignin, tcreateanewaccount;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_login_signup_choice);
        tsignin = findViewById(R.id.signin);
        tcreateanewaccount = findViewById(R.id.createanewaccount);

        fAuth = FirebaseAuth.getInstance();     //for take instance from the our firebase

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
            finish();
        }

        tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t3_login.class));
                finish();
            }
        });
        tcreateanewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t4_signup.class));
                finish();
            }
        });

    }
}