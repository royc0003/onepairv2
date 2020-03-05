package com.iff.onepairv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class ChangeNameActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        mToolbar = (Toolbar) findViewById(R.id.change_name_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Display Name");


    }
}
