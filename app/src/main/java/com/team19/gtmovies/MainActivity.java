package com.team19.gtmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static IOActions ioa;
    protected static View rootView;
    protected static NavigationView navigationView;
    protected static View nav_header;

    // Basic URL for the Tomato API
    // Has two %s placeholders in the middle of baseURL
    private static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?apikey=%s";
    private static String profKey = "yedukp76ffytfuy24zsqk7f5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.MainView1);


        //LOGIN THINGS
        try {
            ioa = new IOActions(this);
        } catch (Exception e) {
            Log.e("GTMovies", e.getMessage());
        }
        if (!IOActions.userSignedIn()) {
            Log.println(Log.INFO, "GTMovies", "not signed in! starting LoginActivity.");
            startActivity(new Intent(this, LoginActivity.class));
            //TODO: onActivityResult which checks if user did login successfully
        }

        //LAYOUT THINGS
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
//        headerView.findViewById(R.id.headerName);
        //((TextView) headerView.findViewById(R.id.headerName))
        //        .setText(IOActions.currentUser.getName());
        //updateNavHeader();

    }

    /**
     * set users info to nav header
     */
    public void updateNavHeader() {
//        Handler tvh = new Handler();
//        Runnable updatetvh = new Runnable() {
//            @Override
//            public void run() {
//                ((TextView) MainActivity.nav_header.findViewById(R.id.headerName))
//                        .setText(IOActions.currentUser.getName());
//                ((TextView) MainActivity.nav_header.findViewById(R.id.headerUsername))
//                        .setText(IOActions.currentUser.getUsername());
//                navigationView.addHeaderView(nav_header);
//            }
//        };
//        tvh.post(updatetvh);

    }

    /**
     * close drawer if open
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.action_logout) {
            IOActions.logoutUser();
            //startActivity(new Intent(this, LoginActivity.class));
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_admin) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
