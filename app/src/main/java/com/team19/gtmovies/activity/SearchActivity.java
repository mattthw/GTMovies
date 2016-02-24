package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.team19.gtmovies.R;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());

        Log.d("GTMovies", "Got to search");

        //set up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.d("GTMovies", "Got past actionBar in onCreate");

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("GTMovies", "Got to onNewIntent");
        handleIntent(intent);
    }

    /**
     * Handles intents in order to fetch query and display results.
     * @param intent intent to manage
     */
    private void handleIntent(Intent intent) {
        Log.d("GTMovies", "Got to handleIntent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d("GTMovies", "if true");
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("GTMovies", "query worked");
            //Set page title to query
            ActionBar actionBar = getSupportActionBar();
            Log.d("GTMovies", "got actionBar");
            if (actionBar != null) {
                Log.e("GTMovies", "Hitting actionBar in handleIntent");
                actionBar.setTitle(query);
            }

            List<Movie> list = MovieListFragment.getMovieList();   //TODO: @Jinu get the movies using query

            MovieListFragment movieListFragment = MovieListFragment.newInstance(0);
            movieListFragment.fillSearchMovieList(list);
            Log.d("GTMovies", "line1");
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(R.id.search_frame_layout,
                        movieListFragment).commit();
                Log.e("GTMovies", "line2");
            } else {
                Log.e("GTMovies", "No fragmentmanager");
            }
            movieListFragment.fillSearchMovieList(null);    //nulls for next query
        }
    }

    /**public void setupSearch(FragmentManager fragmentManager) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("GTMovies", "break here");
                onNewIntent(getSupportParentActivityIntent());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }*/
}
