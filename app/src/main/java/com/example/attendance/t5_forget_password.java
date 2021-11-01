package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class t5_forget_password extends AppCompatActivity {
    private EditText  temailaddress;
    private Button tgetpassword;
    private TextView tgoback;
    FirebaseAuth fAuth;
    ProgressDialog mprogressDialog;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t5_forget_password);
        temailaddress= findViewById(R.id.emailinput);
        tgetpassword= findViewById(R.id.getpasswordbutton);
        tgoback= findViewById(R.id.gobackbutton);
        mprogressDialog = new ProgressDialog(this);
        fAuth = FirebaseAuth.getInstance();

        tgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        tgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void validate() {
        if (TextUtils.isEmpty(temailaddress.getText().toString().trim())){
            temailaddress.setError("Please enter Email");
            return;
        }else {
            mprogressDialog.show();
            mprogressDialog.setMessage("Sending...");
            mprogressDialog.setCancelable(false);
            resetPass();
        }
    }

    private void resetPass() {
        String resetPass = temailaddress.getText().toString().trim();
        FirebaseAuth.getInstance().sendPasswordResetEmail(resetPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Email Sent", Toast.LENGTH_LONG).show();
                    mprogressDialog.hide();
                }else {
                    mprogressDialog.hide();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
