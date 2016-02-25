package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.R;
import com.team19.gtmovies.SingletonMagic;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected static IOActions ioa;
    protected static View rootView;
    protected static NavigationView navigationView;
    protected static Toolbar toolbar;
    protected static DrawerLayout drawer;
    protected static View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.main_view);

        //startActivity(new Intent(this, MovieListActivity.class));

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

        // Layout toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Layout drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Layout navigation
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);

        // Setup tabs and search
        FragmentManager fragmentManager = getSupportFragmentManager();
        setupTabs(fragmentManager);
        setupSearch(fragmentManager);

        // Populate lists of new movies and top rentals
        getMovies();


        // Place view
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();
    }

    /**
     * set users info to nav header
     */
/*    public void updateNavHeader() {
        Handler tvh = new Handler();
        Runnable updatetvh = new Runnable() {
            @Override
            public void run() {
                ((TextView) MainActivity.nav_header.findViewById(R.id.headerName))
                        .setText(IOActions.currentUser.getName());
                ((TextView) MainActivity.nav_header.findViewById(R.id.headerUsername))
                        .setText(IOActions.currentUser.getUsername());
                navigationView.addHeaderView(nav_header);
            }
        };
        tvh.post(updatetvh);
    }*/

    public void setupTabs(final FragmentManager fragmentManager) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MovieFragmentPagerAdapter pagerAdapter = new MovieFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                onPageSelected(position);
            }

            @Override
            public void onPageSelected(int position) {
                if (MovieListFragment.setTabPosition(position)) {
                    fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                            MovieListFragment.newInstance(0)).commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //don't know what to do here
                onPageSelected(state);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.scroll_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupSearch(FragmentManager fragmentManager) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.main_search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("GTMovies", "break here");
                Log.e("GTMovies", "query: " + searchView.getQuery().toString());
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(SearchManager.QUERY, searchView.getQuery().toString());
                intent.setAction(Intent.ACTION_SEARCH);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    public void getMovies() {
        // Get the movies
        List<Movie> newMovies = getMoviesFromAPI(SingletonMagic.newMovie);
        List<Movie> topRentals = getMoviesFromAPI(SingletonMagic.topRental);
        // Set the View
        List<List<Movie>> listList = new ArrayList<>();
        listList.add(newMovies);
        listList.add(topRentals);
        listList.add(new ArrayList<Movie>()); //dummy arraylist for recommendations
        MovieListFragment.fillTabMovieList(listList);
    }

    public List getMoviesFromAPI(String requestType) {
        //initializing new movieArray to return
        final List<Movie> movieArray = new ArrayList<>();

        // Creating the JSONRequest
        String urlRaw = String.format(
                SingletonMagic.baseURL, requestType, "", SingletonMagic.profKey);

        JsonObjectRequest newMovieRequest = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                        if (resp == null) {
                            Log.e("JSONRequest ERROR", "Null Response Received");
                        }

                        // put movies into a JSONArray
                        JSONArray tmpMovies = null;
                        try {
                            tmpMovies = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies in Main.");
                        }
                        if (tmpMovies == null) {
                            Log.e("Movie Error", "movies JSONArray is null!");
                        }
                        for (int i = 0; i < tmpMovies.length(); i++) {
                            try {
                                movieArray.add(new Movie(tmpMovies.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.e("Movie Error", "Couldn't make Movie" + i + "in Main");
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });

        // Access the RequestQueue through singleton class.
        // Add Requests to RequestQueue
        SingletonMagic.getInstance(this).addToRequestQueue(newMovieRequest);

        // Retrun the finished movieArray ArrayList to be added to movieList
        return movieArray;
    }

    /**
     * close drawer if open
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
        return true;
    }*/

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.e("GTMovies", "Problem here");

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.nav_logout) {
            Log.e("GTMovies", "logout problem");
            IOActions.logoutUser();
            Log.e("GTMovies", "logout problem2");
            //startActivity(new Intent(this, LoginActivity.class));
            Intent intent = getIntent();
            Log.e("GTMovies", "logout problem3");
            finish();
            Log.e("GTMovies", "logout problem");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("GTMovies", "Item selected");
        int id = item.getItemId();

        if (id == R.id.nav_manage_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            IOActions.logoutUser();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
