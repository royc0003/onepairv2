package com.iff.onepairv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

/**
 * Activity upon start-up if user is not logged in
 */
public class StartActivity extends AppCompatActivity {

    /**
     * Button for user to go to the register page
     */
    private Button mRegBtn;
    /**
     * Button for user to go to the login page
     */
    private Button mLoginBtn;
    /**
     * Toolbar at the top of the activity
     */
    private Toolbar mToolbar;

    /**
     * Called when activity is first launched
     * @param savedInstanceState
     */
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

                Intent log_int = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(log_int);

            }
        });
    }
}
