package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class t7_profile extends AppCompatActivity {
    private Toolbar tprofile_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t7_profile);
        tprofile_toolbar= findViewById(R.id.profile_toolbar);
        tprofile_toolbar.setTitle("Profile");
        setSupportActionBar(tprofile_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}