package com.example.shajun.letzchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
    }

    public void btnRegistration_Click(View v) {
        Intent i = new Intent(GetStartedActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
    public void btnLogin_Click(View v) {
        Intent i = new Intent(GetStartedActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
