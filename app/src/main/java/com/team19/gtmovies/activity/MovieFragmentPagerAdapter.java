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

        Log.e("GTMovies", "constructor");
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("GTMovies", "getItem ");
        Log.e("GTMovies", "Tabs3");
        MovieListFragment fragment = MovieListFragment.newInstance(position);
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_ITEM_ID, position);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("Jinu MFragPagerAdapt",  Arrays.toString(Thread.currentThread().getStackTrace()));
        //way of returning each title to populate tabs
        Log.e("GTMovies", "getPageTitle " + tabTitles);
        return tabTitles[position];
    }

 }
