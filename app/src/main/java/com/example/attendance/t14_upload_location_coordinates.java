package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class t14_upload_location_coordinates extends AppCompatActivity implements LocationListener {
    private Toolbar locationToolbar;
    private TextView firstT, secondT, thirdT, fourthT;
    private Button firstB, secondB, thirdB, fourthB, submitAllB;
    //    private EditText roomno;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t14_upload_location_coordinates);

        locationToolbar = findViewById(R.id.locationToolbar);
        locationToolbar.setTitle("Location Coordinate");
        setSupportActionBar(locationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fourthT = findViewById(R.id.fourthText);
//        firstT = findViewById(R.id.firstText);
//        secondT = findViewById(R.id.secondText);
//        thirdT = findViewById(R.id.thirdText);
//
//        firstB = findViewById(R.id.firstBut);
//        secondB = findViewById(R.id.secondBut);
//        thirdB = findViewById(R.id.thirdBut);
//        fourthB = findViewById(R.id.fourthBut);
//
//        roomno = findViewById(R.id.roomNo);

        findLocation();
        submitAllB = findViewById(R.id.submitCoordinates);
//        firstB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        secondB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        thirdB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        fourthB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

//        submitAllB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(roomno.getText().toString().trim())) {
//                    roomno.setError("Please enter room number");
//                    return;
//                } else {
//                    uploadData();
//                }
//            }
//        });
    }
//
//    private void uploadData() {
//    }

    private void findLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 2, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Latitude lat", String.valueOf(location.getLatitude()));
        Log.d("Latitude long", String.valueOf(location.getLongitude()));

        fourthT.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        submitAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CornerNum = getIntent().getStringExtra("CornerNumber");

                Log.d("Latitude latdd", String.valueOf(location.getLatitude()));
                Log.d("Latitude longdd", String.valueOf(location.getLongitude()));
                Intent intent = new Intent(t14_upload_location_coordinates.this, t15_get_all_locations.class);
                intent.putExtra("OldActivity", "ParticularLocation");
                intent.putExtra("CornerNumber", CornerNum);
                intent.putExtra("Latitude", String.valueOf(location.getLatitude()));
                intent.putExtra("Longitude", String.valueOf(location.getLatitude()));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "Status");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude", "enable");

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude", "disable");

    }
}