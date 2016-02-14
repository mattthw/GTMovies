package com.team19.gtmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final User cu = MainActivity.ioa.currentUser;
        final IOActions ioa = MainActivity.ioa;

        final EditText eUsername = (EditText)findViewById(R.id.editTextUserProfileUsername);
        final EditText eName = (EditText)findViewById(R.id.editTextUserProfileName);
        final EditText ePassword = (EditText)findViewById(R.id.editTextUserProfilePassword);
        final EditText eBio = (EditText)findViewById(R.id.editTextUserProfileBio);

        // Grab the user's pre-existing information
        eUsername.setText(cu.getUsername());
        eName.setText(cu.getName());
        ePassword.setText(cu.getPassword());
        eBio.setText(cu.getBio());

        // If the user doesn't already have a profile, make sure to let them know.
        if(!cu.getHasProfile()) {
            CharSequence text = ((TextView)findViewById(R.id.textViewUserProfileTitle)).getText();
            ((TextView)findViewById(R.id.textViewUserProfileTitle)).setText(text + " - NO CURRENT PROFILE");
        }

        // What to do if the save button is clicked
        ((Button)findViewById(R.id.buttonUserProfileSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the user entry from IOAction's account list (we will add it back later)
                ioa.getAccounts().remove(cu);

                // Save everything to the currentuser object
                cu.setName(eName.getText().toString());
                cu.setUsername(eUsername.getText().toString());
                cu.setPassword(ePassword.getText().toString());
                cu.setBio(eBio.getText().toString());
                cu.setHasProfile(true); // They saved their new info, so profile is made automatically for them.

                // Save everything to disk
                try {
                    ioa.saveUser();
                    ioa.getAccounts().add(cu);
                    ioa.saveAccounts();
                } catch (FileNotFoundException f) {
                    Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
                } catch (IOException i) {
                    Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
                } catch (Exception e) {
                    Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
                }
            }
        });
    }
}
