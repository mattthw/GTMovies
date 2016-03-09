package com.team19.gtmovies.data;

import android.util.Log;

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
     * Obtains default list of recommendations
     * @return default list of recommendations
     */
    public static List getRecommendations() {
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
     * Obtains list of recommendations by parameter
     * @param code filter for list
     * @return list of movie recommendations
     */
    public static List getRecommendations(int code) {
        //TODO: implement using switch
        switch (code) {
            case BY_MAJOR:
                return getRecommendationsByMajor();
            default:
                return null;
        }
    }

    private static List getRecommendationsByMajor() {
        List<Movie> list = new ArrayList<>();
        Set<Movie> movieSet = IOActions.getMovies();  //note the shallow copy
        String major = CurrentState.getUser().getMajor();
        for (Movie movie : movieSet) {
            if (movie.getUserRatingByMajor(major) >= 0) {
                list.add(movie);
            }
        }
        return list;
    }
}
