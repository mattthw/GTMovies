package com.team19.gtmovies.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.team19.gtmovies.fragment.MovieListFragment;

/**
 * Created by Austin Leal on 2/23/2016.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private static String tabTitles[] = new String[] {"New Movies",
            "Top Rentals", "Your Recommendations"};
    private Context context;

    /**
     * Constructor for MovieFragmentPagerAdapter
     * @param fragmentManager Fragment manager
     * @param context context
     */
    public MovieFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;

        Log.e("GTMovies", "constructor");
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("GTMovies", "getItem");
        return MovieListFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //way of returning each title to populate tabs
        Log.e("GTMovies", "getPageTitle");
        return tabTitles[position];
    }

 }
