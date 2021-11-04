package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;


import com.example.attendance.LoginSystem.t2_login_signup_choice;
import com.example.attendance.LoginSystem.t4_signup;
import com.example.attendance.facedetection.LCOFaceDetection;
import com.example.attendance.facedetection.ResultDilog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class t6_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout tdrawerLayout;
    private NavigationView tnavigationView;
    private Toolbar tdashboardtoolbar;
    private TextView textView;
    private FirebaseAuth fAuth;


    //    for face detection
    private Button topencam;
    private final static int REQUEST_IMAGE_CAPTURE = 124;
    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t6_dashboard);

        tdrawerLayout = findViewById(R.id.drawer_layout);
        tnavigationView = findViewById(R.id.navigationView);
        tdashboardtoolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        setSupportActionBar(tdashboardtoolbar);
        tdashboardtoolbar.setTitle("Dashboard");
        fAuth = FirebaseAuth.getInstance();


        tnavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, tdrawerLayout, tdashboardtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        tdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        to make navigation clickable
        tnavigationView.setNavigationItemSelectedListener(this);
        tnavigationView.setCheckedItem(R.id.nav_dashboard);


//        for face detection
        // initializing our firebase in main activity
        FirebaseApp.initializeApp(this);
        topencam = findViewById(R.id.getAttendanceButton);
        topencam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else {
                    // if the image is not captured, set a toast to display an error image.
                    Toast.makeText(t6_dashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (tdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard: {
                startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
                break;
            }
            case R.id.nav_logout: {
                fAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), t2_login_signup_choice.class));
                break;
            }
            case R.id.nav_setting: {
                startActivity(new Intent(getApplicationContext(), t8_setting.class));
                break;
            }
            case R.id.nav_profile: {
                startActivity(new Intent(getApplicationContext(), t7_profile.class));
                break;
            }
        }
        return true;
    }


    //    for face detection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // after the image is captured, ML Kit provides an
        // easy way to detect faces from variety of image
        // types like Bitmap

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            detectFace(bitmap);
        }
    }

    // If you want to configure your face detection model
    // according to your needs, you can do that with a
    // FirebaseVisionFaceDetectorOptions object.
    private void detectFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();

        // we need to create a FirebaseVisionImage object
        // from the above mentioned image types(bitmap in
        // this case) and pass it to the model.
        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // It’s time to prepare our Face Detection model.
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    // adding an onSuccess Listener, i.e, in case
                    // our image is successfully detected, it will
                    // append it's attribute to the result
                    // textview in result dialog box.
                    public void onSuccess(
                        List<FirebaseVisionFace> firebaseVisionFaces) {
                            String resultText = "";
                            int i = 1;
                            for (FirebaseVisionFace face : firebaseVisionFaces) {
                                resultText = resultText
                                    .concat("\nFACE NUMBER. " + i + ": ")
                                    .concat( "\nSmile: " + face.getSmilingProbability() * 100 + "%")
                                    .concat( "\nleft eye open: " + face.getLeftEyeOpenProbability() * 100 + "%")
                                    .concat( "\nright eye open " + face.getRightEyeOpenProbability() * 100 + "%");
                                    i++;
                        }

                        // if no face is detected, give a toast
                        // message.
                        if (firebaseVisionFaces.size() == 0) {
                            Toast.makeText(t6_dashboard.this,"NO FACE DETECT", Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString( LCOFaceDetection.RESULT_TEXT,resultText);
                            DialogFragment resultDialog = new ResultDilog();
                            resultDialog.setArguments(bundle);
                            resultDialog.setCancelable(true);
                            resultDialog.show(getSupportFragmentManager(),LCOFaceDetection.RESULT_DIALOG);
                        }
                    }
                }) // adding an onfailure listener as well if
                // something goes wrong.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(t6_dashboard.this,"Oops, Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}