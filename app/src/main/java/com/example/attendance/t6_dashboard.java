package com.example.attendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.attendance.LoginSystem.t2_login_signup_choice;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;

public class t6_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout tdrawerLayout;
    NavigationView tnavigationView;
    Toolbar tdashboardtoolbar;
    TextView textView;
    private FirebaseAuth fAuth;

    //    for camera
    private Button topencam;
    private final static int REQUEST_IMAGE_CAPTURE = 124;
    //for face detection
    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;
//    private RecyclerView trecyclerview;

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

// open camera
        topencam = findViewById(R.id.opencam);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // after the image is captured, ML Kit provides an easy way to detect faces from variety of image types like Bitmap

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            detectFace(bitmap);

        }
    }

    private void detectFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();
        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {

                for (FirebaseVisionFace face : firebaseVisionFaces) {
                    Rect bounds = face.getBoundingBox();
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                    FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                    FirebaseVisionFaceLandmark leftChick = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                    FirebaseVisionFaceLandmark rightChick = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
                    FirebaseVisionFaceLandmark mouthBottom = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
                    FirebaseVisionFaceLandmark mouthLeft = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
                    FirebaseVisionFaceLandmark mouthRight = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
                    FirebaseVisionFaceLandmark noseBase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
                    FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                    FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);

                    if (leftEar != null && rightEar != null && leftChick != null && rightChick != null ) {
                        FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                        FirebaseVisionPoint rightEarPos = rightEar.getPosition();
                        FirebaseVisionPoint leftChickPos = leftChick.getPosition();
                        FirebaseVisionPoint rightChickPos = rightChick.getPosition();
                        FirebaseVisionPoint mouthBottomPos = mouthBottom.getPosition();
                        FirebaseVisionPoint mouthLeftPos = mouthLeft.getPosition();
                        FirebaseVisionPoint mouthRightPos = mouthRight.getPosition();
                        FirebaseVisionPoint noseBasePos = noseBase.getPosition();
                        FirebaseVisionPoint leftEyePos = leftEye.getPosition();
                        FirebaseVisionPoint rightEyePos = rightEye.getPosition();


                        int leftEarLandmarkType = leftEar.getLandmarkType();
                        int rightEarLandmarkType = rightEar.getLandmarkType();
                        int leftChickType = leftChick.getLandmarkType();
                        int rightChickType = rightChick.getLandmarkType();

                        int mouthBottomTyp = mouthBottom.getLandmarkType();
                        int mouthLeftTyp = mouthLeft.getLandmarkType();
                        int mouthRightTyp = mouthRight.getLandmarkType();
                        int noseBaseTyp = noseBase.getLandmarkType();
                        int leftEyeTyp = leftEye.getLandmarkType();
                        int rightEyeTyp = rightEye.getLandmarkType();


                        int leftChickHashcode = leftChick.hashCode();
                        int rightChickHashcode = rightChick.hashCode();
                        String leftChickToString = rightChick.toString();
                        String rightChickToString = rightChick.toString();


                        Log.d("mine  leftEarPos", String.valueOf(leftEarPos));
                        Log.d("mine  rightEarPos", String.valueOf(rightEarPos));
                        Log.d("mine  leftChickPos", String.valueOf(leftChickPos));
                        Log.d("mine  rightChickPos", String.valueOf(rightChickPos));
                        Log.d("mine  mouthBottomPos", String.valueOf(mouthBottomPos));
                        Log.d("mine  mouthLeftPos", String.valueOf(mouthLeftPos));
                        Log.d("mine  mouthRightPos", String.valueOf(mouthRightPos));
                        Log.d("mine  noseBasePos", String.valueOf(noseBasePos));
                        Log.d("mine  leftEyePos", String.valueOf(leftEyePos));
                        Log.d("mine  rightEyePos", String.valueOf(rightEyePos));

                        Log.d("mine  mouthBottomTyp", String.valueOf(mouthBottomTyp));
                        Log.d("mine  mouthLeftTyp", String.valueOf(mouthLeftTyp));
                        Log.d("mine  mouthRightTyp", String.valueOf(mouthRightTyp));
                        Log.d("mine  noseBaseTyp", String.valueOf(noseBaseTyp));
                        Log.d("mine  leftEyeTyp", String.valueOf(leftEyeTyp));
                        Log.d("mine  rightEyeTyp", String.valueOf(rightEyeTyp));

                        Log.d("mine  leftEarType", String.valueOf(leftEarLandmarkType));
                        Log.d("mine  rightEarType", String.valueOf(rightEarLandmarkType));
                        Log.d("mine  leftChickType", String.valueOf(leftChickType));
                        Log.d("mine  rightChickType", String.valueOf(rightChickType));
                        Log.d("mine  leftChickHashcode", String.valueOf(leftChickHashcode));
                        Log.d("mine  riChickHashcode", String.valueOf(rightChickHashcode));


                        Log.d("mine  leftChickToString", leftChickToString);
                        Log.d("mine  rChickToString", rightChickToString);







                        String minebreak  = "___________________________________________________";
                        Log.d("mine  this_is_break", minebreak);
                    }

                    // If contour detection was enabled:

                    List<FirebaseVisionPoint> leftEyeContour = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                    List<FirebaseVisionPoint> upperLipBottomContour = face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();

//                    FirebaseVisionPoint left = leftEyeContour.get(57);
//                    Log.d("miiiiinw", String.valueOf(left));


                    // If classification was enabled:
                    if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        float smileProb = face.getSmilingProbability();
                        Log.d("smileeee", String.valueOf(smileProb));
                    }

                    if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        float rightEyeOpenProb = face.getRightEyeOpenProbability();
                    }

                    // If face tracking was enabled:
                    if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                        int id = face.getTrackingId();
                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(t6_dashboard.this, "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
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
}