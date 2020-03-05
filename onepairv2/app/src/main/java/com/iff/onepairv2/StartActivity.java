package com.iff.onepairv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn;
    private Button mLoginBtn;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mToolbar = (Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("1Pair");

        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mLoginBtn = (Button) findViewById(R.id.start_login_btn);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg_int = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_int);

            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent log_int = new Intent(StartActivity.this, Login.class);
                startActivity(log_int);

            }
        });
    }
}
