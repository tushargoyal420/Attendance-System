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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.attendance.model.LocationModel;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class t16_verify_location extends AppCompatActivity implements LocationListener {
    private Toolbar verifyLocationtoolbar;
    private Button getBut, verifyBut, uploadBut;
    private TextView tlati, tlongi;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference getLocationRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t16_verify_location);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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

        Log.d("MineValu11__", "__");
        Log.d("MineValu11_Total", String.valueOf(total));
        Log.d("MineValu11_A", String.valueOf(A));
        Log.d("MineValu11__", "__");

        double difference = total - A;
        Log.d("MineValu11_Diff", String.valueOf(difference));
//        return (difference< .010000);

        return (A == A1 + A2 + A3 + A4);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitudeValue = location.getLatitude();
        double longitudeValue = location.getLongitude();
        tlati.setText(""+ location.getLatitude());
        tlongi.setText(""+location.getLongitude());

        Intent intent = getIntent();
        HashMap<String, String> studentHashMap = (HashMap<String, String>) intent.getSerializableExtra("StudentDetailsHashMap");
        String RoomNo = studentHashMap.get("Room");

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
                                Double FirstLatitude = Double.valueOf(location.getFirstLatitude());  //x1
                                Double FirstLongitude = Double.valueOf(location.getFirstLongitude()); //y1
                                Double SecondLatitude = Double.valueOf(location.getSecondLatitude());//x2
                                Double SecondLongitude = Double.valueOf(location.getSecondLongitude());//y2
                                Double ThirdLatitude = Double.valueOf(location.getThirdLatitude()); //x3
                                Double ThirdLongitude = Double.valueOf(location.getThirdLongitude()); //y3
                                Double FourthLatitude = Double.valueOf(location.getFourthLatitude()); //x4
                                Double FourthLongitude = Double.valueOf(location.getFourthLongitude()); //y4

                                try {
                                    if (check(FirstLatitude, FirstLongitude, SecondLatitude, SecondLongitude, ThirdLatitude, ThirdLongitude, FourthLatitude, FourthLongitude, latitudeValue, longitudeValue)) {
                                        SimpleToast.ok(t16_verify_location.this, "Location verified");
                                        uploadBut.setVisibility(View.VISIBLE);
                                        locationManager.removeUpdates(t16_verify_location.this);
                                    } else {
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
//        uploadBut.setVisibility(View.VISIBLE);
//        uploadBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


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