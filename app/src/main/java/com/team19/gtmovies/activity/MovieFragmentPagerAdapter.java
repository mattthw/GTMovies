package com.team19.gtmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.team19.gtmovies.fragment.MovieListFragment;

import java.util.Arrays;

/**
 * Created by Austin Leal on 2/23/2016.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private static String tabTitles[] = new String[] {"New Movies",
            "Top Rentals", "Your Recommendations"};
    private MovieListFragment[] movieListFragments = new MovieListFragment[3];
    private static boolean change;
    private Context context;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Constructor for MovieFragmentPagerAdapter
     * @param fragmentManager Fragment manager
     * @param context context
     */
    public MovieFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;

//        for (int i = 0; i < 1; i++) {
//            for (int j = 0; j < 1000; j++) {
//                Log.i("Useless", "What Does it all mean?");
//            }
//            for (int j = 0; j < 1000; j++) {
//                Log.i("Useless", "The game of life");
//            }
//        }

        Log.e("GTMovies", "constructor");
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("GTMovies", "getItem position=" + position);
        Log.i("GTMovies", "Tabs3");
        MovieListFragment fragment;
        if (position < 3) {
            MovieListFragment newFragment = MovieListFragment.newInstance(position);
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ITEM_ID, position);
            newFragment.setArguments(arguments);
            if (movieListFragments[position] != null) {
                //return the old, allowing for it to be removed elsewhere
                fragment = movieListFragments[position];
            } else {
                //return the new
                fragment = newFragment;
            }
            movieListFragments[position] = newFragment;
        } else {
            fragment = MovieListFragment.newInstance(position);
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ITEM_ID, position);
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("Jinu MFragPagerAdapt",  Arrays.toString(Thread.currentThread().getStackTrace()));
        //way of returning each title to populate tabs
        Log.i("GTMovies", "getPageTitle " + tabTitles);
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        //if (!change) {
            //return super.getItemPosition(object);
        //} else {
            return POSITION_NONE;
        //}
    }

    /**
     * Tells adapter to modify view
     */
    public static void setChange() {
        change = true;
    }
}
