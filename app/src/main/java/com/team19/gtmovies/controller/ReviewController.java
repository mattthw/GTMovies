package com.team19.gtmovies.controller;

import android.util.Log;

import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Genre;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by a on 3/8/2016.
 * used to get recommended movies for current user
 */
public class ReviewController {
    //Constants for how to obtain RECOMMENDATIONS
    public static final int BY_MAJOR = 1;
    public static final int BY_GENRE = 2;
    public static final int BY_RATING = 3;
    public static final int BY_MAJOR_GENRE = 4;
    public static final int BY_MAJOR_RATING = 5;
    public static final int BY_GENRE_RATING = 6;
    public static final int BY_MAJOR_GENRE_RATING = 7;

    /**
     * Obtains list of RECOMMENDATIONS by parameter
     *
     * @param code filter for list
     *             1: MAJOR
     *             2: GENRE
     *             3: RATING
     *             4: MAJOR_GENRE
     *             5: MAJOR_RATING
     *             6: GENRE_RATING
     *             7: MAJOR_GENRE_RATING
     * @param genre genre to search by if necessary, otherwise null
     * @param rating rating to search by if necessary, otherwise null
     * @return list of movie RECOMMENDATIONS
     */
    public static List<Movie> getRecommendations(int code, Genre genre, Rating rating) {
        //TODO: implement using switch
        if (code == BY_MAJOR) {
            return getRecommendationsByMajor();
        } else if (code == BY_GENRE) {
            return getRecommendationsByGenre(genre);
        }  else if (code == BY_RATING) {
            return getRecommendationsByRating(rating);
        } else if (code == BY_MAJOR_GENRE) {
            return getRecommendationsByMajorGenre(genre);
        } else if (code == BY_MAJOR_RATING) {
            return getRecommendationsByMajorRating(rating);
        } else if (code == BY_GENRE_RATING) {
            return getRecommendationsByGenreRating(genre, rating);
        } else if (code == BY_MAJOR_GENRE_RATING) {
            return getRecommendationsByMajorGenreRating(genre, rating);
        } else {
            return getRecommendations();
        }
    }

    /**
     * Obtains default list of RECOMMENDATIONS
     * (requires user average rating greater than
     * specified amount)
     *
     * @return default list of RECOMMENDATIONS
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
     *
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByMajor() {
        if (CurrentState.getUser() == null) {
            return null;
        }
        List<Movie> list = new ArrayList<>();
        Set<Movie> movieSet = IOActions.getMovies();
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

    /**
     * Recommends Movies depending on the Movies liked by the chosen genre
     *
     * @param searchGenre Genre to search by
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByGenre(Genre searchGenre) {
        List<Movie> recommendationList = getRecommendations();
        List<Movie> byGenreList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            for (Genre genre : movie.getGenres()) {
                if (genre.equals(searchGenre)) {
                    byGenreList.add(movie);
                    break;
                }
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byGenreList;
    }

    /**
     * Recommends Movies depending on the Movies liked by the rating
     *
     * @param rating Rating rating to search by
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByRating(Rating rating) {
        List<Movie> recommendationList = getRecommendations();
        List<Movie> byRatingList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            if (movie.getMPAARating().equals(rating)) {
                byRatingList.add(movie);
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byRatingList;
    }

    /**
     * Recommends Movies depending on the Movies liked by major and chosen genre
     *
     * @param searchGenre Genre to search for
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByMajorGenre(Genre searchGenre) {
        if (CurrentState.getUser() == null) {
            return null;
        }
        List<Movie> recommendationList = getRecommendationsByMajor();
        List<Movie> byMajorGenreList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            for (Genre genre : movie.getGenres()) {
                if (genre.equals(searchGenre)) {
                    byMajorGenreList.add(movie);
                    break;
                }
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byMajorGenreList;
    }

    /**
     * Recommends Movies depending on the Movies liked by major and rating
     *
     * @param rating Rating rating to search for
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByMajorRating(Rating rating) {
        if (CurrentState.getUser() == null) {
            return null;
        }
        List<Movie> recommendationList = getRecommendationsByMajor();
        List<Movie> byMajorRatingList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            if (movie.getMPAARating().equals(rating)) {
                byMajorRatingList.add(movie);
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byMajorRatingList;
    }

    /**
     * Recommends Movies depending on the Movies liked by major and rating
     *
     * @param searchGenre Genre to search by
     * @param rating Rating rating to search for
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByGenreRating(Genre searchGenre, Rating rating) {
        if (CurrentState.getUser() == null) {
            return null;
        }
        List<Movie> recommendationList = getRecommendations();
        List<Movie> byGenreRatingList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            if (movie.getMPAARating().equals(rating)) {
                for (Genre genre : movie.getGenres()) {
                    if (genre.equals(searchGenre)) {
                        byGenreRatingList.add(movie);
                        break;
                    }
                }
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byGenreRatingList;
    }

    /**
     * Recommends Movies depending on the Movies liked by major, genre, and rating
     *
     * @param searchGenre Genre to search by
     * @param rating Rating rating to search for
     * @return List of recommended Movies
     */
    private static List<Movie> getRecommendationsByMajorGenreRating(Genre searchGenre,
            Rating rating) {
        if (CurrentState.getUser() == null) {
            return null;
        }
        List<Movie> recommendationList = getRecommendationsByMajor();
        List<Movie> byGenreRatingList = new ArrayList<>();

        for (Movie movie : recommendationList) {
            if (movie.getMPAARating().equals(rating)) {
                for (Genre genre : movie.getGenres()) {
                    if (genre.equals(searchGenre)) {
                        byGenreRatingList.add(movie);
                        break;
                    }
                }
            }
        }

        Log.i("GTMovies getRec", "rec " + recommendationList.toString());
        return byGenreRatingList;
    }
}
