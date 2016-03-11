package com.team19.gtmovies.controller;

import android.util.Log;

import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by a on 3/8/2016.
 */
public class ReviewController {
    //Constants for how to obtain recommendations
    public static final int BY_MAJOR = 1;
    public static final int BY_GENRE = 2;
    public static final int BY_RATING = 3;

    /**
     * Obtains list of recommendations by parameter
     * @param code filter for list
     *             1: MAJOR
     *             2: GENRE
     *             3: RATING (default)
     * @return list of movie recommendations
     */
    public static List<Movie> getRecommendations(int code) {
        //TODO: implement using switch
        switch (code) {
            case BY_MAJOR:
                return getRecommendationsByMajor();
            case BY_GENRE:
                //fallthrough
            case BY_RATING:
                //fallthrough
            default:
                return getRecommendations();
        }
    }

    /**
     * Obtains default list of recommendations
     * (requires user average rating greater than
     *      specified amount)
     * @return default list of recommendations
     */
    public static List<Movie> getRecommendations() {
        List<Movie> list = new ArrayList<>();
        Set<Movie> movieSet = IOActions.getMovies();  //note the shallow copy
        Log.d("GTMovies", "we gitin 2 dis met??" + IOActions.getMovies().size());
        if (movieSet != null) {
            for (Movie movie : movieSet) {
                if (movie.getUserRating() >= 0) {
                    Log.v("GTMovies getRec add", "[mode:default] " + movie.getUserRating());
                    list.add(movie);
                }
            }
        }
        Log.i("GTMovies getRec", "rec " + list.toString());
        return list;
    }

    /**
     * Recommends Movies depending on the Movies liked by the User's major
     * @return List of recommended Movies
     */
    private static List getRecommendationsByMajor() {
        List<Movie> list = new ArrayList<>();
        Set<Movie> movieSet = IOActions.getMovies();  //note the shallow copy
        String major = CurrentState.getUser().getMajor();
        for (Movie movie : movieSet) {
            int score = movie.getUserRatingByMajor(major);
            if (score >= 0) {
                Log.v("GTMovies rec", "[mode:major(" + major + ")] " + score);
                list.add(movie);
            }
        }
        Log.i("GTMovies getRec", "rec " + list.toString());
        return list;
    }
}
