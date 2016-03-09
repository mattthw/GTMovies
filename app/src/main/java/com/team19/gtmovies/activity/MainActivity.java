package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.CurrentState;
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
import java.util.Set;

/**
 * The Main Activity
 * @author Matt McCoy
 * @version 3.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected static IOActions ioa;
    protected static View rootView;
    protected static NavigationView navigationView;
    protected static Toolbar toolbar;
    protected static DrawerLayout drawer;
    protected static View navHeader;
    protected static FragmentManager fragmentManager;
    protected static CriteriaActivity criteriaActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.main_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(),
                R.color.colorPrimaryDark));

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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Layout navigation
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);

        // Setup tabs and search
        fragmentManager = getSupportFragmentManager();
        //criteriaActivity = (CriteriaActivity) findViewById(R.id.criteria_bar);
        setupTabs();
        setupSearch();

        // Populate lists of new movies and top rentals
        //getMovies();
        getMoviesFromAPI(SingletonMagic.newMovie, null);
        //getMoviesFromAPI(SingletonMagic.topRental, null);

        // Change John Smith to username
        ((TextView) navHeader.findViewById(R.id.headerName)).setText(CurrentState.getUser().getName());

        // Place view
        /*MovieListFragment.setTabs();
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();*/
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

    /**
     * Sets up the TabView
     */
    public void setupTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MovieFragmentPagerAdapter pagerAdapter = new MovieFragmentPagerAdapter(
                getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            LinearLayout criteriaBar = (LinearLayout) findViewById(R.id.criteria_bar);
            //Sliding animations to use for the additional criteria bar in recommendations
            /*Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down);
            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up);*/

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                switch (position) {
                    case MovieListFragment.TOP_RENTALS_TAB:
                        if (!MovieListFragment.hasTopRentalsList()) {
                            getMoviesFromAPI(SingletonMagic.topRental, null);
                        } //otherwise fall through
                    case MovieListFragment.NEW_MOVIES_TAB:
                        //MovieListFragment has already been displayed. Display again.
                        MovieListFragment movieListFragment = MovieListFragment.newInstance(position);
                        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                movieListFragment).commit();
                        break;
                    case MovieListFragment.YOUR_RECOMMENDATIONS_TAB:
                        scroller();
                        criteriaBar.setVisibility(View.VISIBLE);
                        if (!MovieListFragment.hasYourRecommendationsList()) {
                            getMoviesFromAPI(SingletonMagic.recommendations, getRecommendations());
                        } else {
                            movieListFragment = MovieListFragment.newInstance(position);
                            fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                    movieListFragment).commit();
                        }
                        break;
                    default:
                        Log.e("GTMovies", "Incorrect int for tab.");
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case MovieListFragment.TOP_RENTALS_TAB:
                        if (!MovieListFragment.hasTopRentalsList()) {
                            getMoviesFromAPI(SingletonMagic.topRental, null);
                        } //otherwise fall through
                    case MovieListFragment.NEW_MOVIES_TAB:
                        //MovieListFragment has already been displayed. Display again.
                        MovieListFragment movieListFragment = MovieListFragment.newInstance(position);
                        fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                movieListFragment).commit();
                        break;
                    case MovieListFragment.YOUR_RECOMMENDATIONS_TAB:
                        scroller();
                        criteriaBar.setVisibility(View.VISIBLE);
                        if (!MovieListFragment.hasYourRecommendationsList()) {
                            getMoviesFromAPI(SingletonMagic.recommendations, getRecommendations());
                        } else {
                            movieListFragment = MovieListFragment.newInstance(position);
                            fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                    movieListFragment).commit();
                        }
                        break;
                    default:
                        Log.e("GTMovies", "Incorrect int for tab.");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //don't know what to do here
                Log.e("GTMovies", "Tabs this one called. State: " + state);
                //MovieListFragment.setTabPosition(state);
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
        final List<Movie> movieArray = new ArrayList<>();

        // Creating the JSONRequest
        JsonObjectRequest movieRequest = null;
        if (requestType.equals(SingletonMagic.recommendations)) {
            //The last url request. Used to compare to find last movie in onResponse
            final String lastURL;
            if (movieList != null && movieList.size() > 0) {
                Movie movie = movieList.get(movieList.size() - 1);
                String movieID = SingletonMagic.search + "/" + movie.getID();
                lastURL = String.format(SingletonMagic.baseURL,
                        movieID, "", SingletonMagic.profKey);
            } else {
                return;
            }

            //Run for each of the recommended movies
            if (movieList != null) {
                for (Movie movie : movieList) {

                    //create the request
                    String movieID = SingletonMagic.search + "/" + movie.getID();
                    final String urlRaw = String.format(
                            SingletonMagic.baseURL, movieID, "", SingletonMagic.profKey);
                    JsonObjectRequest detailRequest = new JsonObjectRequest(Request.Method.GET,
                            urlRaw, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject resp) {
                            if (resp == null) {
                                Log.e("JSONRequest ERROR", "getMovie Null Response Received");
                            }

                            movieArray.add(new Movie(resp));

                            //Check if last
                            if (urlRaw.equals(lastURL)) {
                                //Now update UI
                                MovieListFragment.setYourRecommendationsList(movieArray);
                                MovieListFragment movieListFragment = MovieListFragment.newInstance(
                                        MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
                                fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                        movieListFragment).commit();
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
                }
            }
        } else {
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
                    MovieListFragment movieListFragment;
                    if (requestType.equals(SingletonMagic.newMovie)) {
                        MovieListFragment.setNewMoviesList(movieArray);
                        movieListFragment = MovieListFragment.newInstance(
                                MovieListFragment.NEW_MOVIES_TAB);
                    } else if (requestType.equals(SingletonMagic.newMovie)) {
                        MovieListFragment.setTopRentalsList(movieArray);
                        movieListFragment = MovieListFragment.newInstance(
                                MovieListFragment.NEW_MOVIES_TAB);
                    } else {
                        movieListFragment = null;
                    }

                    Log.e("Austin", "Can not perform this action after onSaveInstanceState");
                    //ERROR: Can not perform this action after onSaveInstanceState
                    //Update UI
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame_layout,
                            movieListFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
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

    /**
     * Retrieves initial recommendations based on highest rating
     * @return list of Movies
     */
    public List getRecommendations() {
        List<Movie> list = new ArrayList<>();
        Set<Movie> movieSet = IOActions.getMovies();  //note the shallow copy
        Log.d("GTMovies", "we gitin 2 dis met??" + IOActions.getMovies().size());
        for (Movie movie : movieSet) {
            Log.d("GTMovies", "any here " + movie.getUserRating());
            if (movie.getUserRating() >= 0) {
                list.add(movie);
            }
        }
        Log.d("GTMovies getRec", "rec " + list.toString());
        return list;
    }

    /**
     *
     * @param movie
     * @param list
     */
    private void getMovie(Movie movie, final List<Movie> list) {
        Log.d("GTMovies", "getMovie Got to handleIntent");
        if (movie == null) {
            return;
        }

        // Creating the JSONRequest
        String movieID = SingletonMagic.search + "/" + movie.getID();
        final String urlRaw = String.format(
                SingletonMagic.baseURL, movieID, "", SingletonMagic.profKey);

        Log.d("getMovie API", urlRaw);
        JsonObjectRequest detailRequest = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                if (resp == null) {
                    Log.e("JSONRequest ERROR", "getMovie Null Response Received");
                }

                // NOTE TO AUSTANG
                // yeah this is it
                // this should initialize id, title, description, critic rating, posterURL, Genres
                // if you need anything else, you have access to:
                //     year, mpaa_rating, runtime, release_dates, abridged_cast, abridged_directors
                //     studio
                //  and a few more things that you really don't need to care about
                //  everything i just stated above you can get through
                //     (theMovie).getFullInfo.getString("the thing you want");
                //  the exceptions would be
                //    release_dates: (theMovie).getFullInfo.getJSONObject("release_dates");
                //    cast && directors: (theMovie).getFullInfo.getJSONArray("the thing you want");
                list.add(new Movie(resp));


                Log.d("getMovie movie success", "rec movie:" + new Movie(resp));
                Log.d("getMovie volley success", "rec movie:" + urlRaw);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getMovie VOLLEY FAIL", "Couldn't getJSON. rec movie:" + urlRaw);
            }
        });

        // Access the RequestQueue through singleton class.
        // Add Requests to RequestQueue
        SingletonMagic.getInstance(this).addToRequestQueue(detailRequest);
    }

    private void scroller() {
        final ViewTreeObserver.OnScrollChangedListener downListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                ((ScrollView) findViewById(R.id.main_view2)).fullScroll(View.FOCUS_DOWN);
            }
        };

        final ViewTreeObserver.OnScrollChangedListener upListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                ((ScrollView) findViewById(R.id.main_view2)).fullScroll(View.FOCUS_UP);
            }
        };

        //the outer scrollView
        final ScrollView outerScrollView = (ScrollView) findViewById(R.id.main_view2);

        outerScrollView.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                    if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0) {
                        if (observer != null) {
                            observer.removeOnScrollChangedListener(upListener);
                            observer.removeOnScrollChangedListener(downListener);
                            observer = outerScrollView.getViewTreeObserver();
                            observer.addOnScrollChangedListener(upListener);
                        } else {
                            observer = outerScrollView.getViewTreeObserver();
                            observer.addOnScrollChangedListener(upListener);
                        }
                    } else if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0) {
                        observer.removeOnScrollChangedListener(upListener);
                        observer.removeOnScrollChangedListener(downListener);
                        observer = outerScrollView.getViewTreeObserver();
                        observer.addOnScrollChangedListener(downListener);
                    } else {
                        observer = outerScrollView.getViewTreeObserver();
                        observer.addOnScrollChangedListener(downListener);
                    }
                    return true;
                }
                return false;
            }
        });
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
        ((TextView) findViewById(R.id.headerName)).setText(CurrentState.getUser().getName());

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