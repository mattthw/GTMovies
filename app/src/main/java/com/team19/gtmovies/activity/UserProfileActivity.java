package com.team19.gtmovies.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Profile Activity for user
 *
 * @author anonymous
 * @version 1.0
 */
public class UserProfileActivity extends AppCompatActivity {
    protected View rootView;
    private static UserProfileActivity upa = null;
    final User cu = CurrentState.getUser();
    final IOActions ioa = MainActivity.ioa;

    private EditText eUsername;
    private Spinner eMajor;
    private String selectedMajor = "";
    private EditText eName;
    private EditText ePassword;
    private EditText eBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        rootView = findViewById(R.id.userProfileRootView);
        upa = this;
        //setup up button on action bar
        setupActionBar();

        //eUsername = (EditText)findViewById(R.id.editTextUserProfileUsername);
        eMajor = (Spinner)findViewById(R.id.spinnerProfileMajor);
        eName = (EditText)findViewById(R.id.editTextUserProfileName);
        //ePassword = (EditText)findViewById(R.id.editTextUserProfilePassword);
        eBio = (EditText)findViewById(R.id.editTextUserProfileBio);
        // Grab the user's pre-existing information
        //eUsername.setText(cu.getUsername());
        eName.setText(cu.getName());
        //ePassword.setText();
        eBio.setText(cu.getBio());
        //spinner
        selectedMajor = cu.getMajor();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.profMajors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eMajor.setAdapter(adapter);
        addListenerOnSpinnerItemSelection();
        if (!selectedMajor.equals("")) {
            int spinnerPosition = adapter.getPosition(selectedMajor);
            eMajor.setSelection(spinnerPosition);
        }

        // If the user doesn't already have a profile, make sure to let them know.
        if(!cu.getHasProfile()) {
            CharSequence text = ((TextView)findViewById(R.id.textViewUserProfileTitle)).getText();
            CreateProfileDialogFragment alert = new CreateProfileDialogFragment();
            alert.setCancelable(false);
            alert.show(getSupportFragmentManager(), "");

        }
        // What to do if the save button is clicked
        findViewById(R.id.buttonUserProfileSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (verifyProfile())
//                    saveProfile();
                saveProfile();
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

    /**
     * Ensure all fields meet registration requirements
     * @return true if acceptable
     */
    private boolean verifyProfile() {
        boolean cancel = false;
        View focusView = null;

        //ALWAYS CHECK THESE FIELDS
        if (TextUtils.isEmpty(eUsername.getText().toString())) {
            //username empty
            eUsername.setError(getString(R.string.error_field_required));
            focusView = eUsername;
            cancel = true;
        } else if (IOActions.getUserByUsername(eUsername.getText().toString()) != null
                && !(eUsername.getText().toString()
                    .equals(CurrentState.getUser().getUsername()))) {
            //username already exists
            eUsername.setError("Username already exists");
            focusView = eUsername;
            cancel = true;
        } else if (TextUtils.isEmpty(ePassword.getText().toString())) {
            //password empty
            ePassword.setError(getString(R.string.error_field_required));
            focusView = ePassword;
            cancel = true;
        } else if (eUsername.getText().toString().length() <= 3) {
            //username length
            eUsername.setError(getString(R.string.error_invalid_email));
            focusView = eUsername;
            cancel = true;
        } else if (ePassword.getText().toString().length() <= 3) {
            //password length
            ePassword.setError(getString(R.string.error_invalid_password));
            focusView = ePassword;
            cancel = true;
        }
        if (cancel) {
            //cancel and focus on bad field
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Saves user profile
     */
    private void saveProfile() {
        // Remove the user entry from IOAction's account list (we will add it back later)
        IOActions.getAccounts().remove(cu);

        // Store the current hasProfile variable (will come in handy for the snackbar)
        boolean hasprofileback = cu.getHasProfile();

        // Save everything to the currentuser object
        cu.setName(eName.getText().toString());
        cu.setMajor(selectedMajor);
        //cu.setUsername(eUsername.getText().toString());
        //cu.setPassword(ePassword.getText().toString());
        cu.setBio(eBio.getText().toString());
        cu.setHasProfile(true); // They saved their new info, so profile is made automatically for them.

        // Save everything to disk
        try {
            IOActions.saveUser();
            IOActions.getAccounts().add(cu);
            IOActions.saveAccounts();
            IOActions.saveMovies();
        } catch (FileNotFoundException f) {
            Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }

        // Disable text fields and make snackbar for visual confirmation
        eName.setEnabled(false);
//        eUsername.setEnabled(false);
//        ePassword.setEnabled(false);
        eBio.setEnabled(false);
        eMajor.setEnabled(false);
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

    /** inner class for alert that user doesn't currently have a profile.
     * (asks if they want to create one)
     * if yes: allow to edit
     * if no: return to main
     *
     * @author anonymous
     * @version 1.0
     */
    public static class CreateProfileDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You do not have a profile.")
                    .setPositiveButton("Create profile", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // allow edit.
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            upa.finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    /**
     * add listener to spinner item
     */
    public void addListenerOnSpinnerItemSelection() {
        eMajor.setOnItemSelectedListener(new MajorSpinnerListener());
    }

    /**
     * Class needed for spinner XML item
     */
    private class MajorSpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
//            Toast.makeText(parent.getContext(), "Selected Country : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
            selectedMajor = parent.getItemAtPosition(pos).toString();
//            Log.println(Log.INFO, "GTMovies", "selected: " + parent.getItemAtPosition(pos).toString());
        }
        @Override
        public void onNothingSelected(AdapterView parent) {
        }
    }



}
