package com.example.attendance.LoginSystem;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.R;
import com.example.attendance.t6_dashboard;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class t3_login extends AppCompatActivity {
    private EditText temailaddress, tpassword;
    private Button tsignin;
    private ImageView tpasswordshow;
    private TextView tcreateaccount , tforgetpassword;
    private  FirebaseAuth fAuth;
    private ProgressDialog mprogressDialog;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t3_login);
        temailaddress = findViewById(R.id.emailinput);
        tpassword = findViewById(R.id.passwordinput);
        tsignin = findViewById(R.id.studentlogin);
        tcreateaccount = findViewById(R.id.createanewaccountpagelink);
        tforgetpassword= findViewById(R.id.gobackbutton);
        tpasswordshow = findViewById(R.id.passwordshowbutton);
        fAuth = FirebaseAuth.getInstance();
        mprogressDialog = new ProgressDialog(this);


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
        tforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t5_forget_password.class));
            }
        });
        tcreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), t4_signup.class));
                finish();
            }
        });

        tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }
        private void validation(){
            if (TextUtils.isEmpty(temailaddress.getText().toString().trim())) {
                temailaddress.setError("Please enter your Email address ");
                return;
            }
            if (TextUtils.isEmpty(tpassword.getText().toString().trim())) {
                tpassword.setError("Please enter your password");
                return;
            } else {
                mprogressDialog.setMessage("Sign in...");
                mprogressDialog.show();
                mprogressDialog.setCancelable(false);
                login();
            }
        }

        private void login() {
            String email = temailaddress.getText().toString().trim();
            String password = tpassword.getText().toString().trim();

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        assert user != null;
                        if (!user.isEmailVerified()) {
                            SimpleToast.error(t3_login.this, "Please verify your email");
                            mprogressDialog.dismiss();
                        } else {
                            String studentemailtype = "[0-9]+@[stu]+\\.+[upes]+\\.+[ac]+\\.+[in]+";
                            String email = temailaddress.getText().toString().trim();
                            if(email.matches(studentemailtype) && email.length()> 0 ){
                                Intent intent = new Intent(t3_login.this, t6_dashboard.class);
                                intent.putExtra("User", "student");
                                startActivity(intent);
//                                startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
                            }else{
                                Intent intent = new Intent(t3_login.this, t6_dashboard.class);
                                intent.putExtra("User", "faculty");
                                startActivity(intent);
                            }
                            mprogressDialog.dismiss();
                            finish();
                        }
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mprogressDialog.dismiss();
                    }
                }
            });
    }
}