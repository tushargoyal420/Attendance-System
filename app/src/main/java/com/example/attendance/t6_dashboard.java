package com.example.attendance;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.attendance.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class t6_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout tdrawerLayout;
    NavigationView tnavigationView;
    private ImageView tCurrentImage;
    private Toolbar tdashboardtoolbar;
    private TextView result_text, theaderUserName , theaderSapId;

    private FirebaseAuth fAuth;
    private Button topencam, tmatchImage;
    private final static int REQUEST_IMAGE_CAPTURE = 124;
    private DatabaseReference DataRef;

    protected Interpreter tflite;
    private int imageSizeX;
    private int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;

    public static Bitmap cropped;

    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t6_dashboard);

        tdrawerLayout = findViewById(R.id.drawer_layout);
        tnavigationView = findViewById(R.id.navigationView);
        tdashboardtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(tdashboardtoolbar);
        tdashboardtoolbar.setTitle("Dashboard");
//      tnavigationView.addHeaderView();

        //drawer
        tnavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, tdrawerLayout, tdashboardtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        tdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        tnavigationView.setNavigationItemSelectedListener(this);
        tnavigationView.setCheckedItem(R.id.nav_dashboard);
        fAuth = FirebaseAuth.getInstance();

        LoadUserData();
        tCurrentImage = findViewById(R.id.currentImage);
        result_text = findViewById(R.id.result);

        tmatchImage = findViewById(R.id.matchImage);
        tmatchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double distance=calculate_distance(ori_embedding,test_embedding);
                Log.e("distance", String.valueOf(distance));
                if(distance<5.0) {
                    result_text.setText("Result : Face match");
                }else{
                    result_text.setText("Result : Face didn't match");
            }}
        });
        // open camera
        topencam = findViewById(R.id.opencam);
        topencam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(t6_dashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initTflite();
    }
    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum =0.0;
        for(int i=0;i<128;i++){
            sum=sum+Math.pow((ori_embedding[0][i]-test_embedding[0][i]),2.0);
        }
        return Math.sqrt(sum);
    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    //Getting original image from firebase
    private void LoadUserData() {
        String CurrentUser = fAuth.getCurrentUser().getUid();
        DataRef = FirebaseDatabase.getInstance().getReference().child("students").child(CurrentUser);
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData user = dataSnapshot.getValue(UserData.class);


//                    theaderUserName.setText(user.getName());
//                    theaderSapId.setText(user.getSapId());

//                    Picasso.get().load(user.getImageUri()).into(tOriginalImage);
                    Picasso.get()
                            .load(user.getImageUri())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap originalBitmap, Picasso.LoadedFrom from) {
                                    Log.i("donec", "code run");
                                    face_detector(originalBitmap, "original");
                                }
                                @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                                @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}});
                }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            tCurrentImage.setImageBitmap(bitmap);
            face_detector(bitmap, "current");
        }
    }

    private void initTflite() {
        try {
            tflite = new Interpreter(loadmodelfile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    //face detection
    public void face_detector(final Bitmap bitmap, final String imagetype) {

        final InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    cropped = Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
                                    get_embaddings(cropped, imagetype);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void get_embaddings(Bitmap bitmap, String imagetype) {

        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap, inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(), embedding);

        if (imagetype.equals("original"))
            ori_embedding = embedding;
        else if (imagetype.equals("current"))
            test_embedding = embedding;
    }
    //    Drawer
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
            }case R.id.nav_logout: {
                fAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), t2_login_signup_choice.class));
                break;
            }case R.id.nav_profile: {
                startActivity(new Intent(getApplicationContext(), t7_profile.class));
                break;
            }case R.id.nav_timeTable: {
                startActivity(new Intent(getApplicationContext(), t9_time_table.class));
                break;
            }
        }return true;
    }
}