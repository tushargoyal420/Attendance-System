package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class t4_signup extends AppCompatActivity {
    private EditText tsapid, temailaddress, tpassword;
    private Button tcreateaccount;
    private TextView tsignin;
    private String userid;
    private ImageView tpasswordshow;
    private Context context = this;
    private ProgressDialog mprogressDialog;
    FirebaseAuth fAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mprogressDialog = new ProgressDialog(this);
        setContentView(R.layout.t4_signup);
        tsapid = findViewById(R.id.sapidInput);
        temailaddress = findViewById(R.id.emailinput);
        tpassword = findViewById(R.id.createpasswordinput);
        tcreateaccount = findViewById(R.id.getpasswordbutton);
        tsignin = findViewById(R.id.gotoSingInLink);
        fAuth = FirebaseAuth.getInstance();
        tpasswordshow = findViewById(R.id.passwordshowbutton);

        tpasswordshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.passwordshowbutton){

                    if(tpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        tpasswordshow.setImageResource(R.drawable.hide3);
                        //Show Password
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
        tcreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }

            private void validation() {
                if(TextUtils.isEmpty(tsapid.getText().toString().trim())){
                    tsapid.setError("Please enter your Sapid ");
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
                }if ((tsapid.getText().toString().trim()).length() < 9){
                    tsapid.setError("Enter a valid sapId");
                    return;
                }if ((tsapid.getText().toString().trim()).length() > 9){
                    tsapid.setError("Enter a valid sapId");
                    return;
                }else {
                    mprogressDialog.setMessage("Registering...");
                    mprogressDialog.show();
                    mprogressDialog.setCancelable(false);
                    registerUser();
                }
            }
            private void registerUser() {
                String sapid =  tsapid.getText().toString().trim();
                String email = temailaddress.getText().toString().trim();
                String password = tpassword.getText().toString().trim();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();

                                        userid = fAuth.getCurrentUser().getUid();
                                        HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("id", userid);
                                        userMap.put("Sapid", sapid);
                                        userMap.put("Email", email);

                                        root = db.getReference("users").child(userid);
                                        root.setValue(userMap);
                                    }else {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        mprogressDialog.hide();
                                    }
                                }});
                            finish();
                            startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
//                            startActivity(new Intent(getApplicationContext(), hometry.class));
                        }else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressDialog.hide();
                        }
                    }
                });
            }
        });

    }
}