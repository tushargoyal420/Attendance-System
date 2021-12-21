package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class t15_get_all_locations extends AppCompatActivity {
    private Toolbar alllocationT;
    private TextView firstT, secondT, thirdT, fourthT;
    private Button firstB, secondB, thirdB, fourthB, submitAllB;
    private EditText roomno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t15_get_all_locations);

        alllocationT = findViewById(R.id.alllocationT);
        alllocationT.setTitle("Location Coordinate");
        setSupportActionBar(alllocationT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstT = findViewById(R.id.firstText);
        secondT = findViewById(R.id.secondText);
        thirdT = findViewById(R.id.thirdText);
        fourthT = findViewById(R.id.fourthText);

        firstB = findViewById(R.id.firstBut);
        secondB = findViewById(R.id.secondBut);
        thirdB = findViewById(R.id.thirdBut);
        fourthB = findViewById(R.id.fourthBut);
        String OldActivity = getIntent().getStringExtra("OldActivity");
        if(OldActivity.equals("Dashboard")){
            String firstLocation = "--";
            String secondLocation = "--";
            String thirdLocation = "--";
            String fourthLocation = "--";
        }if(OldActivity.equals("ParticularLocation")){

            String Latitude = getIntent().getStringExtra("Latitude");
            String Longitude = getIntent().getStringExtra("Longitude");
            String Corner = getIntent().getStringExtra("CornerNumber");
            if(Corner.equals("First")){
                firstT.setText("Latitude:"+Latitude+" and Longitude: "+ Longitude);
            }
            if(Corner.equals("Second")){
                secondT.setText("Latitude:"+Latitude+" and Longitude: "+ Longitude);
            }
            if(Corner.equals("Third")){
                thirdT.setText("Latitude:"+Latitude+" and Longitude: "+ Longitude);
            }
            if(Corner.equals("Fourth")){
                fourthT.setText("Latitude:"+Latitude+"and Longitude: "+ Longitude);
            }
//            String FirstNewLocationNewLatitude = getIntent().getStringExtra("FirstNewLatitude");
//            String FirstNewLocationNewLongitude = getIntent().getStringExtra("FirstNewLongitude");
        }
        roomno = findViewById(R.id.roomNo);

        firstB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t15_get_all_locations.this, t14_upload_location_coordinates.class);
                intent.putExtra("CornerNumber", "First");
                startActivity(intent);
            }
        });
        secondB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t15_get_all_locations.this, t14_upload_location_coordinates.class);
                intent.putExtra("CornerNumber", "Second");
                startActivity(intent);
            }
        });
        thirdB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t15_get_all_locations.this, t14_upload_location_coordinates.class);
                intent.putExtra("CornerNumber", "Third");
                startActivity(intent);
            }
        });
        fourthB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t15_get_all_locations.this, t14_upload_location_coordinates.class);
                intent.putExtra("CornerNumber", "Fourth");
                startActivity(intent);
            }
        });

        submitAllB = findViewById(R.id.submitCoordinates);
        submitAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(roomno.getText().toString().trim())) {
                    roomno.setError("Please enter room number");
                    return;
                } else {
                    uploadData();
                }
            }
        });
    }
    private void uploadData() {
    }

}