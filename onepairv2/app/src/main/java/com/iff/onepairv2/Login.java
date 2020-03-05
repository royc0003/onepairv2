package com.iff.onepairv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mLogEmail;
    private TextInputLayout mLogPassword;
    private Button mLogBtn;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login to 1Pair");

        mLoginProgress = new ProgressDialog(this);

        mLogEmail = (TextInputLayout) findViewById(R.id.log_email);
        mLogPassword = (TextInputLayout) findViewById(R.id.log_password);
        mLogBtn = (Button) findViewById(R.id.login_login_btn);

        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mLogEmail.getEditText().getText().toString();
                String password = mLogPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mLoginProgress.setTitle("Logging in User");
                    mLoginProgress.setMessage("This may take a while");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email, password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(Login.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    mLoginProgress.hide();
                    Toast.makeText(Login.this, "Invalid Email/Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
