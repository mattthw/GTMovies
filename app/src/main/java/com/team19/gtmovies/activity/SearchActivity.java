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
import com.team19.gtmovies.pojo.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.e("GTMovies", "got to search");

        //remove up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles intents in order to fetch query and display results.
     * @param intent intent to manage
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //Set page title to query
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(query);
            }

            List<Movie> list = new ArrayList<>();   //TODO: @Jinu get the movies using query
//            Movie movie = new Movie();          //and here
//            movie.title = query;                        //and here
//            movie.rating = 50;                          //and here
//            movie.description = "description:" + query; //and here
//            list.add(movie);                            //and here

            final FragmentManager fragmentManager = getSupportFragmentManager();
            MovieListFragment movieListFragment = MovieListFragment.newInstance(0);
            movieListFragment.fillSearchMovieList(list);
            fragmentManager.beginTransaction().replace(R.id.search_frame_layout,
                    movieListFragment).commit();
            movieListFragment.fillSearchMovieList(null);    //nulls for next query
        }
    }
}
