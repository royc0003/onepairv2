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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.datatransport.runtime.backends.BackendFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.text.method.PasswordTransformationMethod;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity for users to log in
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Toolbar at the top of the activity
     */
    private Toolbar mToolbar;

    /**
     * Entry field to type in user's email
     */
    private TextInputLayout mLogEmail;
    /**
     * Entry field to type in user's password
     */
    private TextInputLayout mLogPassword;
    /**
     * Button for user to log in with current email and password
     */
    private Button mLogBtn;

    /**
     * Pop-up during log in process
     */
    private ProgressDialog mLoginProgress;

    /**
     * Firebase Authentication object
     */
    private FirebaseAuth mAuth;

    /**
     * Visibility toggle button
     */
    private ImageButton mVisibilityBtn;
    /**
     * Option to toggle the visibility of password
     */
    private boolean showPassword = false;

    /**
     * Called when activity is first launched
     * @param savedInstanceState
     */
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
        mVisibilityBtn = findViewById(R.id.log_visibility_button);

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

        mVisibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!showPassword){
                   mVisibilityBtn.setImageResource(R.drawable.notvisibleicon);
                   mLogPassword.getEditText().setTransformationMethod(null);
                   showPassword = true;
               } else {
                   mVisibilityBtn.setImageResource(R.drawable.visibleicon);
                   mLogPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                   showPassword = false;
                }
            }
        });



    }

    /**
     * Function to authenticate user's log in credentials
     * @param email Email of current user
     * @param password Password of current user
     */
    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Invalid Email/Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
