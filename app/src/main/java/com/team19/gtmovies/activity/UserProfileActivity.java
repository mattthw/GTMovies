package com.team19.gtmovies.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private static UserProfileActivity userProfInstance = null;
    private User cu = CurrentState.getUser();

    private Spinner eMajor;
    private String selectedMajor = "";
    private EditText eName;
    private EditText eBio;
    private String receiveName;
    public static final int HEADER_NAME_UPDATED = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        rootView = findViewById(R.id.userProfileRootView);
        userProfInstance = this;
        setupActionBar();
        //locate views
        eMajor = (Spinner)findViewById(R.id.spinnerProfileMajor);
        eName = (EditText)findViewById(R.id.editTextUserProfileName);
        eBio = (EditText)findViewById(R.id.editTextUserProfileBio);

        //get user if called from mod's user list
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            receiveName = null;
            this.setTitle("Edit Profile");
        } else {
            receiveName = extras.getString("UNAME");
            cu = IOActions.getUserByUsername(receiveName);
            if (CurrentState.getUser().getPermission() == 2) {
                findViewById(R.id.buttonRank).setVisibility(View.VISIBLE);
                this.setTitle("View Profile (as admin)");
            } else {
                //disable editing for regular users
                this.setTitle("View Profile");
                findViewById(R.id.buttonUserProfileSubmit).setVisibility(View.GONE);
                eMajor.setEnabled(false);
                eName.setEnabled(false);
                eBio.setEnabled(false);
            }
        }

        ((TextView)findViewById(R.id.unameView))
                .setText(cu.getUsername());
        updateRank();
        // Grab the user's pre-existing information
        eName.setText(cu.getName());
        eBio.setText(cu.getBio());
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
            //CharSequence text = ((TextView)findViewById(R.id.textViewUserProfileTitle)).getText();
            CreateProfileDialogFragment alert = new CreateProfileDialogFragment();
            alert.setCancelable(false);
            alert.show(getSupportFragmentManager(), "");
        }
        // What to do if the save button is clicked
        findViewById(R.id.buttonUserProfileSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
     * sets the rank XML view to current rank of user
     */
    private void updateRank() {
        String hisRank = null;
        int perm = cu.getPermission();
        if (perm == 2) {
            hisRank = "admin";
        } else if (perm == 1) {
            hisRank = "active";
        }else if (perm == 0) {
            hisRank = "locked";
        } else if (perm == -1) {
            hisRank = "banned";
        }
        ((TextView)findViewById(R.id.rankView)).setText(hisRank);
    }

    /**
     * changes rank to next possible rank
     * incremented by integers. if rank=2 (admin)
     * then set to -1 (banned)
     * @param view view
     */
    public void changeRank(View view) {
        int perm = cu.getPermission();
        if (perm == 2) {
            cu.setPermission(-1);
        } else {
            perm += 1;
            cu.setPermission(perm);
        }
        updateRank();
        Toast.makeText(UserProfileActivity.this, "RANK CHANGED", Toast.LENGTH_SHORT).show();
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
        eBio.setEnabled(false);
        eMajor.setEnabled(false);
        if(!hasprofileback) {
            Snackbar.make(rootView, "Profile successfully created!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(rootView, "Profile has been updated!", Snackbar.LENGTH_SHORT).show();
        }

        if (!eName.getText().toString().equals(CurrentState.getUser().getName())) {
            setResult(HEADER_NAME_UPDATED);
        }
        // We are done. Go back to previous activity, after a set delay.
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
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
                finish();
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
            builder.setMessage(userProfInstance.cu.getUsername() + " does not have a profile.")
                    .setPositiveButton("Create profile", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // allow edit.
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            userProfInstance.finish();
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
            selectedMajor = parent.getItemAtPosition(pos).toString();
        }
        @Override
        public void onNothingSelected(AdapterView parent) {
        }
    }



}
