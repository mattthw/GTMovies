package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.abstractClasses.MovieControlActivity;
import com.team19.gtmovies.controller.MovieControllerTask;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import java.util.List;

/**
 * The Main Activity
 *
 * @author Matt McCoy
 * @version 3.0
 */
public class MainActivity extends MovieControlActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //protected static IOActions ioa;
    protected static View mainRootView;
    protected static NavigationView navigationView;
    protected static Toolbar mToolbar;
    protected static LinearLayout criteriaBar;
    protected static DrawerLayout drawer;
    protected static MovieFragmentPagerAdapter movieFragmentPagerAdapter;
    protected static CriteriaActivity criteriaActivity;
    private static FragmentManager fragmentManager;

    private static final int SEARCH_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 13;

    private String secretQueryDoNotWorryAbout = "";

/*
// Meant for NyanCat
    private boolean mIsBound = false;
    private MusicService mServ;
    private Intent musicthing;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        musicthing = new Intent(this,MusicService.class);
        bindService(musicthing,
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            stopService(musicthing);
            unbindService(Scon);
            mIsBound = false;
        }
    }

    // Put following in code

        doBindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);

    // Do doUnbindService onDestroy
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        if (IOActions.getIOActionsInstance() == null) {
            Log.e("GTMovies", "IOActions.getIOActionsInstance == null !");
            // Go back to SplashScreenActivity
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
            return;
        }
        Log.println(Log.ASSERT, "GTMovies", "current user='" + CurrentState.getUser() + "'");
        if (CurrentState.getUser() == null) {
            //start loginactivity
            Log.println(Log.INFO, "GTMovies", "not signed in! starting LoginActivity.");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        Log.w("GTMovies", "MAIN ACTIVITY ONCREATE!");
        setContentView(R.layout.activity_main);
        mainRootView = findViewById(R.id.main_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));

        // Layout toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        if (findViewById(R.id.app_bar) != null) {
            findViewById(R.id.app_bar).animate().translationY(0).setInterpolator(
                    new DecelerateInterpolator(2));
        }

        // Layout drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Layout navigation
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //update header with current user's name
        updateNavName();

        // Populate lists of new movies and top rentals
        //getMovies();
        //new UpdateUITask().execute(MovieListFragment.TOP_RENTALS_TAB);
        findViewById(R.id.criteria_bar).setVisibility(View.GONE);
        //getMoviesFromAPI(SingletonMagic.TOP_RENTAL, null);
        //getMoviesFromAPI(SingletonMagic.NEW_MOVIE, null);
        //getMoviesFromAPI(SingletonMagic.TOP_RENTAL, null);
        // Setup tabs and SEARCH
        fragmentManager = getSupportFragmentManager();
        //criteriaActivity = (CriteriaActivity) findViewById(R.id.criteria_bar);
        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(findViewById(R.id.content_main);
        params.setMargins(0, R.attr.actionBarSize - 1, 0,0);
        ((RelativeLayout) findViewById(R.id.content_main)).setLayoutParams(params);*/

        // Setup tabs and search
        setupTabs();
        setupSearch();

        Log.d("Actionbar", "size=" + R.attr.actionBarSize);
        // Place view
        MovieListFragment.setTabs();
        /*fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MovieListFragment.newInstance(0)).commit();*/
        MovieListFragment.updateAdapter(MovieListFragment.NEW_MOVIES_TAB);
        MovieListFragment.updateAdapter(MovieListFragment.TOP_RENTALS_TAB);
        MovieListFragment.updateAdapter(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
    }

    /**
     * do things depending on results from activities called.
     *
     * @param requestCode what we are checking
     * @param resultCode  value returned for what being checked
     * @param data        idk
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("GTMovies", "resultCode=" + resultCode);
        if (requestCode == UserProfileActivity.HEADER_NAME_UPDATED) {
            updateNavName();
        } else if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == 1) {
                if (CurrentState.getUser() == null) {
                    Log.e("GTMovies", "go log in!! >:(");
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST_CODE);
                } else {
                    updateNavName();
                }
            } else if (resultCode == -1) {
                Log.e("GTMovies", "login cancelled, quitting app.");
                finish();
                return;
            }

            //debug code from @austin
            if (IOActions.getIOActionsInstance() == null) {
                startActivity(new Intent(this, SplashScreenActivity.class));
                finish();
                Log.e("GTMovies", "MainActivity killed because IOActions NULL!\n DDEAAAAAATHHHH!!!!");
                return;
            }
        } else if (requestCode == SEARCH_REQUEST_CODE) {
            findViewById(R.id.movie_list_view).setPadding(
                    0, R.attr.actionBarSize, 0, 0);
        }
    }

    /**
     * update header name
     */
    public void updateNavName() {
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.headerName);
        if (CurrentState.getUser() != null) {
            name.setText(CurrentState.getUser().getName());
            Log.println(Log.DEBUG, "GTMovies", "header name updated to: " + name);
        } else {
            Log.println(Log.INFO, "GTMovies", "header view null, couldn't update.");
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
                    //Sliding animations to use for the additional criteria bar in recommendations
            /*Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down);


            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up);*/


                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                        if (position == MovieListFragment.YOUR_RECOMMENDATIONS_TAB) {
                            criteriaBar.setVisibility(View.VISIBLE);
                            setupMajorButton();
                        } else {
                            criteriaBar.setVisibility(View.GONE);
                        }
                        /*if (position == MovieListFragment.TOP_RENTALS_TAB) {
                            if (!MovieListFragment.hasTopRentalsList()) {
                                getMoviesFromAPI(SingletonMagic.TOP_RENTAL, null);
                            }
                        } else if (position == MovieListFragment.NEW_MOVIES_TAB) {// MovieListFragment.NEW_MOVIES_TAB:
                                //MovieListFragment has already been displayed. Display again.
                            case MovieListFragment.YOUR_RECOMMENDATIONS_TAB
                                criteriaBar.setVisibility(View.VISIBLE);
                                if (!MovieListFragment.hasYourRecommendationsList()) {
                                    getMoviesFromAPI(SingletonMagic.RECOMMENDATIONS,
                                            ReviewController.getRecommendations());
                                } else {
                                    movieListFragment = MovieListFragment.newInstance(position);
                                    fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                                            movieListFragment).commit();
                                }
                                break;
                            default:
                                Log.e("GTMovies", "Incorrect int for tab.");
                        }*/
                    }

                    @Override
                    public void onPageSelected(int position) {
                        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
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
                Log.d("GTMovies", "break here");
                //Log.d("GTMovies", "query: " + searchView.getQuery().toString());
                String queryURI = Uri.encode(query);
                findViewById(R.id.movie_list_view).setPadding(0, 0, 0, 0);
                (new MovieControllerTask()).execute(MainActivity.this, MovieControllerTask.SEARCH_MOVIES,
                        "q=" + queryURI, null, null);
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
     * Sets up listener for major button
     */
    public void setupMajorButton() {
        Log.d("main", "major button");
        final RelativeLayout majorButton = (RelativeLayout) findViewById(R.id.major_button);
        final TextView majorText = (TextView) findViewById(R.id.major_text);
        final GradientDrawable majorBackground = (GradientDrawable) majorButton.getBackground();


        if (majorButton.getElevation() == R.dimen.raised_elevation) {
            //recommendations = ReviewController.getRecommendations(ReviewController.BY_MAJOR);
        } else {
            //recommendations = ReviewController.getRecommendations();
        }
        //new UpdateUITask().execute(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);

        majorButton.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;
            @Override
            public void onClick(View v) {
                Log.e("main", "major button onClick");
                if (!selected) {
                    new MovieControllerTask().execute(MainActivity.this,
                            MovieControllerTask.UPDATE_RECOMMENDATIONS,
                            MovieControllerTask.RECOMMENDATIONS_BY_MAJOR);
                    majorButton.setElevation(getResources().getDimension(R.dimen.raised_elevation));
                    majorBackground.clearColorFilter();
                    majorBackground.setColorFilter(
                            ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                            PorterDuff.Mode.LIGHTEN);
                    majorText.setTextColor(Color.WHITE);
                    selected = true;
                } else {
                    new MovieControllerTask().execute(MainActivity.this,
                            MovieControllerTask.UPDATE_RECOMMENDATIONS,
                            MovieControllerTask.RECOMMENDATIONS_GENERAL);
                    majorButton.setElevation(getResources().getDimension(R.dimen.flat_elevation));
                    majorBackground.clearColorFilter();
                    majorBackground.setColorFilter(
                            ContextCompat.getColor(getApplicationContext(), R.color.White),
                            PorterDuff.Mode.LIGHTEN);
                    majorText.setTextColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.colorPrimary));
                    selected = false;
                }
            }
        });
    }

    /**
     * Sets up listener for major button
     */
    public void setupGenreButton() {
        Log.d("main", "major button");
        final RelativeLayout genreButton = (RelativeLayout) findViewById(R.id.genre_button);
        final TextView genreText = (TextView) findViewById(R.id.genre_text);
        final GradientDrawable genreBackground = (GradientDrawable) genreButton.getBackground();

        genreButton.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;
            @Override
            public void onClick(View v) {
                Log.e("main", "major button onClick");
                if (!selected) {
                    if (findViewById(R.id.major_button).getElevation()
                            == getResources().getDimension(R.dimen.raised_elevation)) {
                        (new MovieControllerTask()).execute(MainActivity.this,
                                MovieControllerTask.UPDATE_RECOMMENDATIONS,
                                MovieControllerTask.RECOMMENDATIONS_BY_MAJOR_GENRE,
                                null, null);
                    } else {
                        (new MovieControllerTask()).execute(MainActivity.this,
                                MovieControllerTask.UPDATE_RECOMMENDATIONS,
                                MovieControllerTask.RECOMMENDATIONS_BY_GENRE,
                                null, null);
                    }
                    genreButton.setElevation(getResources().getDimension(R.dimen.raised_elevation));
                    genreBackground.clearColorFilter();
                    genreBackground.setColorFilter(
                            ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                            PorterDuff.Mode.LIGHTEN);
                    genreText.setTextColor(Color.WHITE);
                    selected = true;
                } else {
                    if (findViewById(R.id.major_button).getElevation()
                            == getResources().getDimension(R.dimen.raised_elevation)) {
                        (new MovieControllerTask()).execute(MainActivity.this,
                                MovieControllerTask.UPDATE_RECOMMENDATIONS,
                                MovieControllerTask.RECOMMENDATIONS_BY_MAJOR,
                                null, null);
                    } else {
                        (new MovieControllerTask()).execute(MainActivity.this,
                                MovieControllerTask.UPDATE_RECOMMENDATIONS,
                                MovieControllerTask.RECOMMENDATIONS_GENERAL,
                                null, null);
                    }
                    genreButton.setElevation(getResources().getDimension(R.dimen.flat_elevation));
                    genreBackground.clearColorFilter();
                    genreBackground.setColorFilter(
                            ContextCompat.getColor(getApplicationContext(), R.color.White),
                            PorterDuff.Mode.LIGHTEN);
                    genreText.setTextColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.colorPrimary));
                    selected = false;
                }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("GTMovies", "Item selected");
        int id = item.getItemId();

        if (id == R.id.nav_manage_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivityForResult(intent, UserProfileActivity.HEADER_NAME_UPDATED);
        } else if (id == R.id.nav_friends) {
            startActivity(new Intent(this, UserListActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            IOActions.logoutUser();
            //recreate this class. leave this. tested and true works. //TODO: check if I broke it so that I don't suffer the wrath of Supreme Leader's rage
            startActivity(new Intent(this, SplashScreenActivity.class));
            //recreate();
            finish();
        } else {
            return false;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        //nothing
    }

    @Override
    public void finishedGettingMovies(int requestType) {
        //nothing
        Log.e("Main", "finish called");
        if (requestType == MovieControllerTask.SEARCH_MOVIES) {
            if (secretQueryDoNotWorryAbout.equals("Matt McCoy")) {
                List<Movie> list = MovieListFragment.getSearchMovieList();
                list.add(new Movie(-1));
                MovieListFragment.setSearchMovieList(list);
                MovieListFragment.updateAdapter(MovieListFragment.SEARCH);
            }
            Log.e("Main", "request correct");
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(SearchManager.QUERY,
                    ((SearchView) findViewById(R.id.main_search_bar)).getQuery().toString());
            intent.setAction(Intent.ACTION_SEARCH);
            startActivityForResult(intent, SEARCH_REQUEST_CODE);
        }
    }
}