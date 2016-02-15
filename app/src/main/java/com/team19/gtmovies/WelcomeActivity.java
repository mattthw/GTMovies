package com.team19.gtmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * called when "continue" button does onClick
     * @param view from XML/activity
     */
    public void userLogin(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
