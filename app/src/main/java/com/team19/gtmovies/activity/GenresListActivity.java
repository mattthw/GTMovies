package com.team19.gtmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Genre;
import com.team19.gtmovies.pojo.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

/**
 * Created by Austin Leal on 4/26/2016.
 *
 * @author Austin Leal
 * @version 1.0
 */
public class GenresListActivity extends AppCompatActivity {
    public static String SELECTED_GENRE = "selected";

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    /**
     * default oncreate
     * adds actionbar for overflow
     * calls populatList for ListView
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        //setupActionBar();
        populateList();
    }

    /**
     * adds proper rank to users in list
     *
     */
    public static void formatList() {
    }

    /**
     * adds list of users to Listview object in activity
     */
    public void populateList() {
        // Find the ListView resource.
        mainListView = (ListView) findViewById(R.id.userListView);
        // Create ArrayAdapter using the usernames list.
        ArrayList<Genre> genresList = new ArrayList<>(EnumSet.allOf(Genre.class));
        ArrayList<String> fullList = new ArrayList<>(genresList.size());
        for (Genre genre : genresList) {
            fullList.add(genre.toString());
        }
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fullList);
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent userIntent = getIntent();
                Object o = mainListView.getItemAtPosition(position);
                String name = (String) o;   //As you are using Default String Adapter
                if (o == null) {
                    Log.e("Genres", "problem");
                }
                userIntent.putExtra(SELECTED_GENRE, name);
                setResult(RESULT_OK, userIntent);
                GenresListActivity.this.finish();
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            //navigateUpTo(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
