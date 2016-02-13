package com.team19.gtmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ((EditText)findViewById(R.id.editTextUserProfileUsername)).setText(MainActivity.ioa.currentUser.getUsername());
        ((EditText)findViewById(R.id.editTextUserProfileName)).setText(MainActivity.ioa.currentUser.getName());
        ((EditText)findViewById(R.id.editTextUserProfilePassword)).setText(MainActivity.ioa.currentUser.getPassword());
    }
}
