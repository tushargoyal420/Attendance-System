package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class t8_setting extends AppCompatActivity {
    private Toolbar tsetting_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t8_setting);

        tsetting_toolbar= findViewById(R.id.setting_toolbar);
        tsetting_toolbar.setTitle("Settings");
        setSupportActionBar(tsetting_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}