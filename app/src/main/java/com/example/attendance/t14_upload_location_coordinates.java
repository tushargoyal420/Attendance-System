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

import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class t14_upload_location_coordinates extends AppCompatActivity implements LocationListener {
    private Toolbar locationToolbar;
    private TextView firstLongitudeText, firstLatiTudeText, secondLongitudeText, secondLatiTudeText, thirdLongitudeText, thirdLatiTudeText, fourthLongitudeText, fourthLatiTudeText, currentCoordinates;
    private Button firstB, secondB, thirdB, fourthB, submitAllB, refreshButton;
    private EditText roomno;
    private DatabaseReference checkroomLocationRef, roomLocationRef;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t14_upload_location_coordinates);

        locationToolbar = findViewById(R.id.locationToolbar);
        locationToolbar.setTitle("Location Coordinate");
        setSupportActionBar(locationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentCoordinates = findViewById(R.id.currentCoordinates);
        refreshButton = findViewById(R.id.refreshButton);

        firstLatiTudeText = findViewById(R.id.firstLatiTudeText);
        firstLongitudeText = findViewById(R.id.firstLongitudeText);
        secondLatiTudeText = findViewById(R.id.secondLatiTudeText);
        secondLongitudeText = findViewById(R.id.secondLongitudeText);
        thirdLatiTudeText = findViewById(R.id.thirdLatiTudeText);
        thirdLongitudeText = findViewById(R.id.thirdLongitudeText);
        fourthLatiTudeText = findViewById(R.id.fourthLatiTudeText);
        fourthLongitudeText = findViewById(R.id.fourthLongitudeText);

        firstB = findViewById(R.id.firstBut);
        secondB = findViewById(R.id.secondBut);
        thirdB = findViewById(R.id.thirdBut);
        fourthB = findViewById(R.id.fourthBut);

        roomno = findViewById(R.id.roomNo);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });

        submitAllB = findViewById(R.id.submitCoordinates);
        submitAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((firstLongitudeText.getText()).equals("Longitude") || (firstLatiTudeText.getText()).equals("Latitude")) {
                    firstB.setError("Please select first corner location");
                    return;
                }
                if ((secondLongitudeText.getText()).equals("Longitude") || (secondLatiTudeText.getText()).equals("Latitude")) {
                    secondB.setError("Please select second corner location");
                    return;
                }
                if ((thirdLongitudeText.getText()).equals("Longitude") || (thirdLatiTudeText.getText()).equals("Latitude")) {
                    thirdB.setError("Please select third corner location");
                    return;
                }
                if ((fourthLongitudeText.getText()).equals("Longitude") || (fourthLatiTudeText.getText()).equals("Latitude")) {
                    fourthB.setError("Please select fourth corner location");
                    return;
                }
                if (TextUtils.isEmpty(roomno.getText().toString().trim())) {
                    roomno.setError("Please enter room number");
                    return;
                } else {
                    String FirstLatitude = firstLatiTudeText.getText().toString().trim();
                    String FirstLongitude = firstLongitudeText.getText().toString().trim();
                    String SecondLatitude = secondLatiTudeText.getText().toString().trim();
                    String SecondLongitude = secondLongitudeText.getText().toString().trim();
                    String ThirdLatitude = thirdLatiTudeText.getText().toString().trim();
                    String ThirdLongitude = thirdLongitudeText.getText().toString().trim();
                    String FourthLatitude = fourthLatiTudeText.getText().toString().trim();
                    String FourthLongitude = fourthLongitudeText.getText().toString().trim();
                    String roomNo = roomno.getText().toString().trim();
                    uploadData(roomNo, FirstLatitude, FirstLongitude, SecondLatitude, SecondLongitude, ThirdLatitude, ThirdLongitude, FourthLatitude, FourthLongitude);
                }
            }
        });
    }

    private void uploadData(String roomNo, String FirstLatitude, String FirstLongitude, String SecondLatitude, String SecondLongitude, String ThirdLatitude, String ThirdLongitude, String FourthLatitude, String FourthLongitude) {
        HashMap<String, String> coordinates = new HashMap<>();
        coordinates.put("FirstLatitude", FirstLatitude);
        coordinates.put("FirstLongitude", FirstLongitude);
        coordinates.put("SecondLatitude", SecondLatitude);
        coordinates.put("SecondLongitude", SecondLongitude);
        coordinates.put("ThirdLatitude", ThirdLatitude);
        coordinates.put("ThirdLongitude", ThirdLongitude);
        coordinates.put("FourthLatitude", FourthLatitude);
        coordinates.put("FourthLongitude", FourthLongitude);
        try {
            checkroomLocationRef = db.getReference().child("roomlist");
            checkroomLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(roomNo).exists()) {
                        SimpleToast.error(t14_upload_location_coordinates.this, "RoomCoordinates already present");
                    } else {
                        roomLocationRef = db.getReference("roomlist").child(roomNo);
                        roomLocationRef.setValue(coordinates);
                        SimpleToast.ok(t14_upload_location_coordinates.this, "Done");
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Log.e("t14 activity Error", "ExceptionError", e);
        }
    }

    private void findLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentCoordinates.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        firstB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLatiTudeText.setText(String.valueOf(location.getLatitude()));
                firstLongitudeText.setText(String.valueOf(location.getLongitude()));
                firstB.setError(null);
            }
        });
        secondB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondLatiTudeText.setText(String.valueOf(location.getLatitude()));
                secondLongitudeText.setText(String.valueOf(location.getLongitude()));
                secondB.setError(null);
            }
        });
        thirdB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdLatiTudeText.setText(String.valueOf(location.getLatitude()));
                thirdLongitudeText.setText(String.valueOf(location.getLongitude()));
                thirdB.setError(null);
            }
        });
        fourthB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourthLatiTudeText.setText(String.valueOf(location.getLatitude()));
                fourthLongitudeText.setText(String.valueOf(location.getLongitude()));
                fourthB.setError(null);
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


