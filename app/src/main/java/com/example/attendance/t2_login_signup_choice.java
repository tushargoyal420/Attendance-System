package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class t2_login_signup_choice extends AppCompatActivity {
    private Button tsigninButton, tcreatenewaccountbutton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_login_signup_choice);
        tsigninButton = findViewById(R.id.signinbutton);
        tcreatenewaccountbutton = findViewById(R.id.singinbutton);

        fAuth = FirebaseAuth.getInstance();     //for take instance from the our firebase

        if (fAuth.getCurrentUser() != null) {             //if user is already login
            startActivity(new Intent(getApplicationContext(), hometry.class));
            finish();
        }

        tsigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t3_login.class));
            }
        });
        tcreatenewaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t4_signup.class));
            }
        });

    }
}