package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.R;
import com.team19.gtmovies.controller.ReviewController;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.data.SingletonMagic;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Main Activity
 * @author Matt McCoy
 * @version 3.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //protected static IOActions ioa;
    protected static View mainRootView;
    protected static NavigationView navigationView;
    protected static Toolbar toolbar;
    protected static DrawerLayout drawer;
    protected static MovieFragmentPagerAdapter movieFragmentPagerAdapter;
    protected static CriteriaActivity criteriaActivity;

    private static int currentPage;
    private List<Movie> recommendations;
    private boolean generalRecommendations = true;

    public MainActivity() {
        super();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (IOActions.getIOActionsInstance() == null) {
            startActivity(new Intent(this, SplashScreenActivity.class));
            onDestroy();
            return;
        }

        Log.w("MAINACTIVITY", "ONCREATE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

        setContentView(R.layout.activity_main);
        mainRootView = findViewById(R.id.main_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),
                R.color.colorPrimaryDark));

        //LOGIN THINGS
        Log.println(Log.INFO, "GTMovies", "not signed in! starting LoginActivity.");

        // Layout toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Layout drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Layout navigation
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.headerName);
        name.setText(CurrentState.getUser().getName());

        //result updates header. dont fuck it up.
        // BACK to activity_main if the user did indeed log in.
        //has to be done after view stuff setup or else will act on null views
        startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.LOGIN_FINISHED);


        // Populate lists of new movies and top rentals
        //getMovies();
        //new UpdateUITask().execute(MovieListFragment.TOP_RENTALS_TAB);
        currentPage = 0;
        findViewById(R.id.criteria_bar).setVisibility(View.GONE);
        //getMoviesFromAPI(SingletonMagic.topRental, null);
        //getMoviesFromAPI(SingletonMagic.newMovie, null);


        //getMoviesFromAPI(SingletonMagic.topRental, null);
        // Setup tabs and search
        //fragmentManager = getSupportFragmentManager();
        //criteriaActivity = (CriteriaActivity) findViewById(R.id.criteria_bar);
        setupTabs();
        setupSearch();

        // Place view
        MovieListFragment.setTabs();
        /*fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();*/
        MovieListFragment.updateAdapter(MovieListFragment.NEW_MOVIES_TAB);
        MovieListFragment.updateAdapter(MovieListFragment.TOP_RENTALS_TAB);
        MovieListFragment.updateAdapter(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        getMovies();
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMovies();
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();
    }*/


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        //nothing
    }


    /**
     * do things depending on results from activities called.
     * @param requestCode what we are checking
     * @param resultCode value returned for what being checked
     * @param data idk
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UserProfileActivity.HEADER_NAME_UPDATED) {
            updateNavName();
        }
        if (requestCode == LoginActivity.LOGIN_FINISHED) {
            updateNavName();
            /*MovieListFragment.updateAdapter(MovieListFragment.NEW_MOVIES_TAB);
            MovieListFragment.updateAdapter(MovieListFragment.TOP_RENTALS_TAB);
            MovieListFragment.updateAdapter(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
            mainRootView.invalidate();*/
        }
    }

    /**
     * update header name
     */
    public void updateNavName() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.headerName);
        if (name != null) {
            name.setText(CurrentState.getUser().getName());
            Log.println(Log.ASSERT, "GTMovies", "header name updated to: " + name);
        } else {
            Log.println(Log.ERROR,"GTMovies", "header view null, couldn't update.");
        }
    }

    /**
     * Sets up the TabView
     */
    public void setupTabs() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        movieFragmentPagerAdapter = new MovieFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        ((ViewPager) findViewById(R.id.view_pager)).setAdapter(movieFragmentPagerAdapter);
        ((ViewPager) findViewById(R.id.view_pager)).addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
            LinearLayout criteriaBar = (LinearLayout) findViewById(R.id.criteria_bar);
            //Sliding animations to use for the additional criteria bar in endations
            /*Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down);
            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up);*/

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
//                switch (position) {
//                    case MovieListFragment.TOP_RENTALS_TAB:
//                        if (!MovieListFragment.hasTopRentalsList()) {
//                            getMoviesFromAPI(SingletonMagic.topRental, null);
//                        } //otherwise fall through
//                    case MovieListFragment.NEW_MOVIES_TAB:
//                        //MovieListFragment has already been displayed. Display again.
//                        MovieListFragment movieListFragment = MovieListFragment.newInstance(position);
//                        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
//                                movieListFragment).commit();
//                        break;
//                    case MovieListFragment.YOUR_RECOMMENDATIONS_TAB:
//                        scroller();
//                        criteriaBar.setVisibility(View.VISIBLE);
//                        if (!MovieListFragment.hasYourRecommendationsList()) {
//                            getMoviesFromAPI(SingletonMagic.recommendations,
//                                    ReviewController.getRecommendations());
//                        } else {
//                            movieListFragment = MovieListFragment.newInstance(position);
//                            fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
//                                    movieListFragment).commit();
//                        }
//                        break;
//                    default:
//                        Log.e("GTMovies", "Incorrect int for tab.");
//                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

                //set new height options
                if (CurrentState.getOpenHeight() == 0) {
                    CurrentState.setOpenHeight(viewPager.getHeight());
                    CurrentState.setClosedHeight(viewPager.getHeight() + toolbar.getHeight());
                    Log.e("CurrentState", "height=" + CurrentState.getOpenHeight() + " height=" + CurrentState.getClosedHeight());
                }
                Log.e("CurrentState", "height=" + CurrentState.getOpenHeight() + " height=" + CurrentState.getClosedHeight());

                switch (position) {
                    case MovieListFragment.NEW_MOVIES_TAB:
                        criteriaBar.setVisibility(View.GONE);
                        //new UpdateUITask().execute(position);
                        break;
                    case MovieListFragment.TOP_RENTALS_TAB:
                        criteriaBar.setVisibility(View.GONE);
                        //new UpdateUITask().execute(position);
                        break;
                    case MovieListFragment.YOUR_RECOMMENDATIONS_TAB:
                        criteriaBar.setVisibility(View.VISIBLE);
                        //viewPager.getLayoutParams().height = CurrentState.getOpenHeight()
                        //        - R.dimen.text_margin;
                        List<Movie> newRecommendations;
                        setupMajorButton();
                        break;
                    default:
                        Log.e("GTMovies", "Incorrect int for tab.");
                }
                Log.e("CurrentState2", "height=" + CurrentState.getOpenHeight() + " height=" + CurrentState.getClosedHeight());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("GTMovies", "Tabs this one called. State: " + state);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.scroll_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Sets up search widget
     */
    public void setupSearch() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.main_search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentPage = MovieListFragment.SEARCH;
                Log.d("GTMovies", "break here");
                //Log.d("GTMovies", "query: " + searchView.getQuery().toString());
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(SearchManager.QUERY, searchView.getQuery().toString());
                intent.setAction(Intent.ACTION_SEARCH);
                startActivity(intent);
                Log.d("GTMovies", "skipped it");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    /**
     * Sets up listener for genre button
     */
    public void setupMajorButton() {
        boolean selected = false;
        Log.d("main", "major button");
        final RelativeLayout majorButton = (RelativeLayout) findViewById(R.id.major_button);
        final TextView majorText = (TextView) findViewById(R.id.major_text);
        final GradientDrawable majorBackground = (GradientDrawable) majorButton.getBackground();


        if (majorButton.getElevation() == R.dimen.raised_elevation) {
            recommendations = ReviewController.getRecommendations(ReviewController.BY_MAJOR);
        } else {
            recommendations = ReviewController.getRecommendations();
        }
        new UpdateUITask().execute(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);

        majorButton.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;

            @Override
            public void onClick(View v) {
                Log.e("main", "major button onClick");
                if (!selected) {
                    majorButton.setElevation(getResources().getDimension(R.dimen.raised_elevation));
                    majorBackground.setColorFilter(
                            ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                            PorterDuff.Mode.LIGHTEN);
                    majorText.setTextColor(Color.WHITE);
                    recommendations = ReviewController.getRecommendations(ReviewController.BY_MAJOR);
                    selected = true;
                } else {
                    majorButton.setElevation(getResources().getDimension(R.dimen.flat_elevation));
                    majorBackground.clearColorFilter();
                    majorText.setTextColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.colorPrimary));
                    recommendations = ReviewController.getRecommendations();
                    selected = false;
                }
                new UpdateUITask().execute(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
            }
        });
    }

    /**
     * Fills lists of movies for tabs.
     */
    public void getMovies() {
        /*// Get the movies
        List<Movie> newMovies = getMoviesFromAPI(SingletonMagic.newMovie);
        List<Movie> topRentals = getMoviesFromAPI(SingletonMagic.topRental);
        List<Movie> recommendations = getRecommendations();
        // Set the View
        List<List<Movie>> listList = new ArrayList<>();
        listList.add(newMovies);
        listList.add(topRentals);
        listList.add(recommendations);
        listList.add(new ArrayList<Movie>()); //dummy arraylist for recommendations
        MovieListFragment.fillTabMovieList(listList);
        */
    }

    /**
     * Obtains the movies from the API
     * @param requestType differetiates new movies and top rental
     * @param movieList list of movies to get details about for recommendations
     */
    private void getMoviesFromAPI(final String requestType, final List<Movie> movieList) {
        //initializing new movieArray to return
        Log.d("getMoviesFromAPI", "request" + requestType);

        // Creating the JSONRequest
        JsonObjectRequest movieRequest = null;
        if (requestType.equals(SingletonMagic.recommendations)) {
            Log.d("getMoviesFromAPI", "recommendations");
            if (movieList == null || movieList.size() <= 0) {
                return;
            }

            final Map<Integer, Movie> movieMap = new HashMap<>();

            //Run for each of the recommended movies
            for (final Movie movie : movieList) {

                //create the request
                String movieID = SingletonMagic.search + "/" + movie.getID();
                final String urlRaw = String.format(
                        SingletonMagic.baseURL, movieID, "", SingletonMagic.profKey);
                movieRequest = new JsonObjectRequest(Request.Method.GET,
                        urlRaw, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                        if (resp == null) {
                            Log.e("JSONRequest ERROR", "getMovie Null Response Received");
                        }

                        Movie mMovie = new Movie(resp);
                        movieMap.put(mMovie.getID(), mMovie);

                        //Check if last
                        if (movieMap.size() >= movieList.size() - 3) {
                            //Now update UI
                            List<Movie> movieArray = new ArrayList<>();
                            movieArray.addAll(movieMap.values());
                            MovieListFragment.setYourRecommendationsList(movieArray);
                            updateUI(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
                        }

                        Log.d("getMovie movie success", "rec movie:" + new Movie(resp));
                        Log.d("getMovie volley success", "rec movie:" + urlRaw);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getMovie VOLLEY FAIL", "Couldn't getJSON. rec movie:" + urlRaw);
                    }
                });
                SingletonMagic.getInstance(this).addToRequestQueue(movieRequest);
            }
        } else {
            final List<Movie> movieArray = new ArrayList<>();
            final String urlRaw = String.format(
                    SingletonMagic.baseURL, requestType, "", SingletonMagic.profKey);

            movieRequest = new JsonObjectRequest
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

                            //Create proper MovieListFragment
                            String tab;
                            if (requestType.equals(SingletonMagic.newMovie)) {
                                Log.d("JinuMain", "newMovieFragment");

                                MovieListFragment.setNewMoviesList(movieArray);
                                updateUI(MovieListFragment.NEW_MOVIES_TAB);
                                //MovieListFragment.setTopRentalsList(movieArray);        //TODO:Remove
                        /*movieListFragment = MovieListFragment.newInstance(
                                MovieListFragment.NEW_MOVIES_TAB);)*/

                                tab = "newMovies";
                            } else if (requestType.equals(SingletonMagic.topRental)) {
                                Log.d("JinuMain", "topRentalFragment");
                                MovieListFragment.setTopRentalsList(movieArray);
                                updateUI(MovieListFragment.TOP_RENTALS_TAB);
                        /*movieListFragment = MovieListFragment.newInstance(
                                MovieListFragment.TOP_RENTALS_TAB);*/
                                tab = "topRentals";
                            } else {
                                Log.d("JinuMain", "nullFragment");
                                //movieListFragment = null;
                                tab = "nullFragment";
                            }

                            Log.d("Main", "getMoviesFromAPI tab " + tab + " calling updateUI");
                            // Added the executePend....() thing to onScrollStateChanged
                            //
                            Log.d("JinuMain", "End of request");
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY FAIL", "Couldn't getJSON");
                        }
                    });
        }

        // Access the RequestQueue through singleton class.
        // Add Requests to RequestQueue
        SingletonMagic.getInstance(this).addToRequestQueue(movieRequest);
    }



























    private class UpdateUITask extends AsyncTask<Integer, Integer, Integer> {
        private List<Movie> movieList;

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                case MovieListFragment.YOUR_RECOMMENDATIONS_TAB:
                    getMoviesFromAPI(SingletonMagic.recommendations,
                            recommendations);
                    break;
                default:
            }
            for (int i = 0; i < 1; i++) {
                Log.i("Useless", "What is Android? What is Android? What is Android? What is Android?");
            }
            for (int i = 0; i < 1; i++) {
                Log.i("Useless", "Do Androids Dream of Electric Sheep?");
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
        }
    }


    /**
     * Updates user interface
     * @param page which tab to update
     */
    public void updateUI(int page) {
        Log.e("GTMovies", "updateUI");
        MovieListFragment.updateAdapter(page);

        findViewById(R.id.movie_detail_container).invalidate();
        findViewById(R.id.movie_list_view).invalidate();
        findViewById(R.id.movie_list).invalidate();
        findViewById(R.id.view_pager).invalidate();
        findViewById(R.id.main_linear_layout).invalidate();
        findViewById(R.id.main_view2).invalidate();
        findViewById(R.id.content_main).invalidate();
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
        // Change John Smith to username

        if (id == R.id.nav_manage_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivityForResult(intent, UserProfileActivity.HEADER_NAME_UPDATED);
        } else if (id == R.id.nav_friends) {
            startActivity(new Intent(this, UserListActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            IOActions.logoutUser();
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //public static void setIOA(IOActions actions) {
    //    ioa = actions;
    //}


}