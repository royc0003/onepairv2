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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mRegBtn;

    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");

        mRegProgress = new ProgressDialog(this);

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_displayname);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mRegBtn = (Button) findViewById(R.id.reg_login_btn);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                //check fields are not empty before registering
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                   mRegProgress.setTitle("Registering User");
                   mRegProgress.setMessage("This may take a while");
                   mRegProgress.setCanceledOnTouchOutside(false);
                   mRegProgress.show();
                   register_user(display_name, email, password);
                }

            }
        });
    }

    private void register_user(final String display_name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://128.199.167.80:8080/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    BackEndController backEndController = retrofit.create(BackEndController.class);
                    Call<Void> call = backEndController.addUser(uid);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(!response.isSuccessful()){
                                System.out.println("Oops something went wrong!");
                                return;
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("Oops something went wrong!");
                        }
                    });

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("image", "https://firebasestorage.googleapis.com/v0/b/onepairv2.appspot.com/o/profile_images%2Fdefault.png?alt=media&token=3d4d5d85-b050-481b-87ab-c0ae09dbda32");
                    userMap.put("thumb_image", "https://firebasestorage.googleapis.com/v0/b/onepairv2.appspot.com/o/profile_images%2Fdefault.png?alt=media&token=3d4d5d85-b050-481b-87ab-c0ae09dbda32");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRegProgress.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });
                }
                else{
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Invalid Email/Password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
