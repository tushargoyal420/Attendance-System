package com.example.attendance.LoginSystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance.R;
import com.example.attendance.t6_dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class t4_signup extends AppCompatActivity {
    private EditText tname, temailaddress, tpassword;
    private Button tcreateaccount, mselectprofileimage;
    private TextView tsignin;
    private String userid;
    private ImageView tpasswordshow;
    private Context context = this;
    private ProgressDialog mprogressDialog;
    FirebaseAuth fAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    private StorageReference reference;
    private CircleImageView meditprofilepicture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mprogressDialog = new ProgressDialog(this);
        setContentView(R.layout.t4_signup);
        tname = findViewById(R.id.nameInput);
        temailaddress = findViewById(R.id.emailinput);
        tpassword = findViewById(R.id.createpasswordinput);
        tcreateaccount = findViewById(R.id.studentlogin);
        tsignin = findViewById(R.id.gotoSingInLink);
        fAuth = FirebaseAuth.getInstance();
        tpasswordshow = findViewById(R.id.passwordshowbutton);



        tpasswordshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.passwordshowbutton){

                    if(tpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        tpasswordshow.setImageResource(R.drawable.hide3);

                        tpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        tpasswordshow.setImageResource(R.drawable.show3);
                        //Hide Password
                        tpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            }
        });
        tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t3_login.class));
                finish();
            }
        });
        reference = FirebaseStorage.getInstance().getReference();

        meditprofilepicture= findViewById(R.id.editprofilepicture);
        mselectprofileimage= findViewById(R.id.selectprofileimage);
        mselectprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        tcreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }

            private void validation() {
                if(TextUtils.isEmpty(tname.getText().toString().trim())){
                    tname.setError("Please enter your Name ");
                    return;
                }if (TextUtils.isEmpty(temailaddress.getText().toString().trim())) {
                    temailaddress.setError("Please enter email");
                    return;
                }if (TextUtils.isEmpty(tpassword.getText().toString().trim())) {
                    tpassword.setError("Please enter password");
                    return;
                }if ((tpassword.getText().toString().trim()).length() < 6) {
                    tpassword.setError("Enter password more then 6 characters");
                    return;
                }else {
                    mprogressDialog.setMessage("Registering...");
                    mprogressDialog.show();
                    mprogressDialog.setCancelable(false);

                    String studentemailtype = "[0-9]+@[stu]+\\.+[upes]+\\.+[ac]+\\.+[in]+";
//                    String facultyemailtype = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    //if we have faculty email then we add below pattern for faculty's email  but we don't have
                    //faculty email so we are using normal email vaildation for faculty
                    //String forfaculty = "[a-zA-Z0-9._-]+@[ddn]+\\.+[upes]+\\.+[ac]+\\.+[in]+";

                    String email = temailaddress.getText().toString().trim();
                    if(email.matches(studentemailtype) && email.length()> 0 ){
                        String currentUser = "student";
                        registerUser(currentUser, imageUri);
                    }else{
                        String currentUser = "faculty";
                        registerUser(currentUser, imageUri);
                    }
                }
            }



        private void registerUser(String currentUser, Uri uri) {
            String name =  tname.getText().toString().trim();
            String email = temailaddress.getText().toString().trim();
            String password = tpassword.getText().toString().trim();
            String emailname   = email.substring(0, email.lastIndexOf("@"));
            String domain = email.substring(email.lastIndexOf("@") +1);

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                                    fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    userid = fAuth.getCurrentUser().getUid();

                                                    Map<String, Object> useData = new HashMap<>();
                                                    useData.put("id", userid);
                                                    useData.put("name", name);
                                                    useData.put("Email", email);
                                                    useData.put("imageUri",uri.toString());
                                                     if(String.valueOf(currentUser).equals("student")) {
                                                        String sapid= emailname;
                                                        useData.put("SapId", sapid);
                                                        root = db.getReference("students").child(userid);
                                                    }
                                                    else{
                                                        root = db.getReference("faculty").child(userid);
                                                    }
                                                    root.setValue(useData);
                                                }
                                            });
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(t4_signup.this, "Uploading Failed!.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {

                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mprogressDialog.hide();
                                }
                            }});
//                        Intent intent=new Intent(t4_signup.this, t6_dashboard.class);
//                        intent.putExtra("currentSapId",emailname);
//                        startActivity(intent);
                        startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
                        finish();
                    }else{
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mprogressDialog.hide();
                    }
                }
            });
        }
    });
    }

    private void chooseimage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            meditprofilepicture.setImageURI(imageUri);
        }
    }
    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}