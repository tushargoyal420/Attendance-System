package com.example.attendance;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class t1_splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t1_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( t1_splash_screen.this, t2_login_signup_choice.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}