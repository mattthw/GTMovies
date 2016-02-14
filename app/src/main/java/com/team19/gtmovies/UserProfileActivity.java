package com.team19.gtmovies;

import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UserProfileActivity extends AppCompatActivity {
    protected View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        rootView = findViewById(R.id.userProfileRootView);

        final User cu = MainActivity.ioa.currentUser;
        final IOActions ioa = MainActivity.ioa;

        final EditText eUsername = (EditText)findViewById(R.id.editTextUserProfileUsername);
        final EditText eName = (EditText)findViewById(R.id.editTextUserProfileName);
        final EditText ePassword = (EditText)findViewById(R.id.editTextUserProfilePassword);
        final EditText eBio = (EditText)findViewById(R.id.editTextUserProfileBio);

        //setup up button on action bar
        setupActionBar();

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

                // Store the current hasProfile variable (will come in handy for the snackbar)
                boolean hasprofileback = cu.getHasProfile();

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

                // Disable text fields and make snackbar for visual confirmation
                eName.setEnabled(false);
                eUsername.setEnabled(false);
                ePassword.setEnabled(false);
                eBio.setEnabled(false);
                if(!hasprofileback) {
                    Snackbar.make(rootView, "Profile successfully created!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(rootView, "Profile has been updated!", Snackbar.LENGTH_SHORT).show();
                }

                // We are done. Go back to MainActivity, after a set delay.
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        NavUtils.navigateUpFromSameTask(UserProfileActivity.this);
                    }
                };
                Timer t = new Timer();
                t.schedule(task, 1000);
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}