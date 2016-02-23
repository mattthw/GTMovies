package com.team19.gtmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.team19.gtmovies.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * called when "Login" button does onClick
     * @param view from XML/activity
     */
    public void userLogin(View view) {
        int success = 1;
        setResult(success, new Intent().putExtra("login", true));
        finish();
    }

    /**
     * called when "Register" button does onClick
     * @param view from XML/activity
     */
    public void userRegister(View view) {
        int success = 1;
        setResult(success, new Intent().putExtra("login", false));
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
