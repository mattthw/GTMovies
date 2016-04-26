package com.team19.gtmovies.abstractClasses;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Austin Leal on 4/25/2016.
 *
 * @author Austin Leal
 * @version 1.0
 */
public abstract class MovieControlActivity extends AppCompatActivity{

    /**
     * Called after calling MovieControllerTask
     *
     * @param requestType type of request. Given by constants in MovieControllerTask.
     */
    public abstract void finishedGettingMovies(int requestType);
}
