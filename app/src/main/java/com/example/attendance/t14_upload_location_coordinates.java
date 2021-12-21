package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
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
    private EditText roomno;
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

        firstT = findViewById(R.id.firstText);
        secondT = findViewById(R.id.secondText);
        thirdT = findViewById(R.id.thirdText);
        fourthT = findViewById(R.id.fourthText);

        firstB = findViewById(R.id.firstBut);
        secondB = findViewById(R.id.secondBut);
        thirdB = findViewById(R.id.thirdBut);
        fourthB = findViewById(R.id.fourthBut);

        roomno = findViewById(R.id.roomNo);

        submitAllB = findViewById(R.id.submitCoordinates);


        firstB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        secondB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        thirdB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fourthB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation("Fourth");
            }
        });

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

    private void findLocation(String number) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        fourthT.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
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