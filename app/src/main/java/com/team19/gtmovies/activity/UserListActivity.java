package com.team19.gtmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.fragment.MovieListFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class UserListActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private Intent userIntent = null;

    //TODO: JAVADOCS!!!!!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setupActionBar();
        populateList();
        userIntent = new Intent(this, UserProfileActivity.class);
        String strName = null;
        userIntent.putExtra("UNAME", strName);
    }

    private void populateList() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.userListView );
        ArrayList<String> usernameList = IOActions.getUsernames();
        Collections.sort(usernameList);
        usernameList.remove(CurrentState.getUser().getUsername());
        String hisRank = "";
        for (int i = 0; i < usernameList.size(); i++) {
            int perm = IOActions.getUserByUsername(usernameList.get(i)).getPermission();
            if (perm == 2) {
                hisRank = "[A]";
            } else if (perm == 1) {
                hisRank = "[U]";
            }else if (perm == 0) {
                hisRank = "[L]";
            } else if (perm == -1) {
                hisRank = "[B]";
            }
            usernameList.set(i, hisRank + "   " + usernameList.get(i));
        }
        // Create ArrayAdapter using the usernames list.
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernameList);
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = mainListView.getItemAtPosition(position);
                String name=(String)o;//As you are using Default String Adapter
                name = name.substring(name.lastIndexOf(" ") + 1);
                userIntent.putExtra("UNAME", name);
                if (IOActions.getUserByUsername(name) != null) {
                    startActivityForResult(userIntent, UserProfileActivity.PROFILE_VIEWED);
                } else {
                    Toast.makeText(getBaseContext(),name + " does not exist!", Toast.LENGTH_SHORT).show();
                }
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
     * do things depending on results from activities called.
     * @param requestCode what we are checking
     * @param resultCode value returned for what being checked
     * @param data idk
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UserProfileActivity.PROFILE_VIEWED) {
            populateList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
