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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.attendance.model.LocationModel;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class t16_verify_location extends AppCompatActivity implements LocationListener {
    private Toolbar verifyLocationtoolbar;
    private Button getBut, verifyBut, uploadBut;
    private TextView tlati, tlongi;
    protected LocationManager locationManager;
    protected Context context;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference getLocationRef;
    private SimpleDateFormat timestampFormat;
    private Calendar calendar;
    String timestamp;
    FirebaseAuth fAuth;
    DatabaseReference uploadData;
    private static final int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t16_verify_location);
        timestampFormat = new SimpleDateFormat("hh:mm:ss");
        calendar = Calendar.getInstance();
        timestamp = timestampFormat.format(calendar.getTime());
        fAuth = FirebaseAuth.getInstance();

        verifyLocationtoolbar = findViewById(R.id.verifyLocationtoolbar);
        verifyLocationtoolbar.setTitle("Verify location");
        setSupportActionBar(verifyLocationtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getBut = findViewById(R.id.getButton);
        verifyBut = findViewById(R.id.verifyButton);
        uploadBut = findViewById(R.id.uploadButton);
        tlati = findViewById(R.id.currentLatitudeTextView);
        tlongi = findViewById(R.id.currentLongitudeTextView);

        getBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoordinate();
            }
        });
    }

    private void getCoordinate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }

    static double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return (double) Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

    static boolean check(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x, double y) {
        double A = area(x1, y1, x2, y2, x3, y3) + area(x1, y1, x4, y4, x3, y3);
        double A1 = area(x, y, x1, y1, x2, y2);
        double A2 = area(x, y, x2, y2, x3, y3);
        double A3 = area(x, y, x3, y3, x4, y4);
        double A4 = area(x, y, x1, y1, x4, y4);
        double total = A1 + A2 + A3 + A4;
        double difference = total - A;

        return (difference < 0.000000003);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitudeValue = location.getLatitude();
        double longitudeValue = location.getLongitude();
        tlati.setText("" + location.getLatitude());
        tlongi.setText("" + location.getLongitude());

        Intent intent = getIntent();
        HashMap<String, Object> studentHashMap = (HashMap<String, Object>) intent.getSerializableExtra("StudentDetailsHashMap");
        String RoomNo = (String) studentHashMap.get("Room");


        verifyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RoomNo.equals("")) {
                    getLocationRef = FirebaseDatabase.getInstance().getReference("roomlist").child(RoomNo);
                    getLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                LocationModel location = snapshot.getValue(LocationModel.class);
                                double FirstLatitude = Double.parseDouble(location.getFirstLatitude());
                                double FirstLongitude = Double.parseDouble(location.getFirstLongitude());
                                double SecondLatitude = Double.parseDouble(location.getSecondLatitude());
                                double SecondLongitude = Double.parseDouble(location.getSecondLongitude());
                                double ThirdLatitude = Double.parseDouble(location.getThirdLatitude());
                                double ThirdLongitude = Double.parseDouble(location.getThirdLongitude());
                                double FourthLatitude = Double.parseDouble(location.getFourthLatitude());
                                double FourthLongitude = Double.parseDouble(location.getFourthLongitude());

                                try {
                                    if (check(FirstLatitude, FirstLongitude, SecondLatitude, SecondLongitude, ThirdLatitude, ThirdLongitude, FourthLatitude, FourthLongitude, latitudeValue, longitudeValue)) {
                                        SimpleToast.ok(t16_verify_location.this, "Location verified");
                                        uploadBut.setVisibility(View.VISIBLE);
                                        uploadBut.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                studentHashMap.put("LocationLatitude", String.valueOf(latitudeValue));
                                                studentHashMap.put("LocationLongitude", String.valueOf(longitudeValue));
                                                studentHashMap.put("LocationTimeStamp", timestamp);
                                                String CurrentUser = fAuth.getCurrentUser().getUid();
                                                String Date = (String) studentHashMap.get("Date");
                                                String Subject = (String) studentHashMap.get("Subject");
                                                String Branch = getIntent().getStringExtra("BranchForMarking");
                                                String TimePeriod = getIntent().getStringExtra("TimePeridMarking");
                                                uploadData = FirebaseDatabase.getInstance().getReference().child("Attendance").child(Branch).child(Date).child(Subject).child(TimePeriod);
                                                uploadData.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        uploadData.child(CurrentUser).updateChildren(studentHashMap);
//                                                        mprogressDialog.dismiss();
                                                        SimpleToast.ok(t16_verify_location.this, "Marked");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent intent = new Intent(t16_verify_location.this, t6_dashboard.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }, SPLASH_TIME_OUT);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        SimpleToast.error(t16_verify_location.this, "Error to Mark Attendance");
//                                                        mprogressDialog.dismiss();
                                                    }
                                                });

                                            }
                                        });
                                        locationManager.removeUpdates(t16_verify_location.this);
                                    } else {
                                        uploadBut.setVisibility(View.GONE);
                                        SimpleToast.error(t16_verify_location.this, "Location not verified");
                                        locationManager.removeUpdates(t16_verify_location.this);
                                    }

                                } catch (Exception e) {
                                }
                            } else {
                                SimpleToast.error(t16_verify_location.this, "No location found of this class");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            SimpleToast.error(t16_verify_location.this, "Error to load data");
                        }
                    });
                }
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