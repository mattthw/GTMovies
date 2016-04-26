package com.team19.gtmovies.data;

import com.team19.gtmovies.controller.ReviewController;
import com.team19.gtmovies.pojo.Genre;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Rating;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Austin Leal on 4/20/2016.
 *
 * @author Austin Leal
 * @version 1.0
 */
public class PageData {
    private static int newMoviesCurrentPage = 0;
    private static int topRentalsCurrentPage = 0;
    private static int recommendationsCurrentPage = 0;

    private static List<Movie> generalRecommendations = ReviewController.getRecommendations();
    private static List<Movie> majorRecommendations = null;
    private static List<Movie> genreRecommendations = null;
    private static List<Movie> ratingRecommendations = null;
    private static List<Movie> majorGenreRecommendations = null;
    private static List<Movie> majorRatingRecommendations = null;
    private static List<Movie> genreRatingRecommendations = null;
    private static List<Movie> majorGenreRatingRecommendations = null;


    /**
     * Sets current new movies page back to zero.
     */
    public static void zeroNewMoviesPage() {
        newMoviesCurrentPage = 0;
    }

    /**
     * Increments new movies page.
     *
     * @return old new movies page
     */
    public static int nextNewMoviesPage() {
        return newMoviesCurrentPage++;
    }

    /**
     * Sets current top rentals page back to zero.
     */
    public static void zeroTopRentalsPage() {
        topRentalsCurrentPage = 0;
    }

    /**
     * Increments top rentals page.
     *
     * @return old top rentals page
     */
    public static int nextTopRentalsPage() {
        return topRentalsCurrentPage++;
    }

    /**
     * Sets current recommendations page back to zero.
     */
    public static void zeroRecommendationsPage() {
        recommendationsCurrentPage = 0;
    }

    /**
     * Increments recommendations page.
     *
     * @return old recommendations page
     */
    public static int nextrecommendationsPage() {
        return recommendationsCurrentPage++;
    }


    /**
     * Getter for general recommendations list
     *
     * @return generalRecommendations list
     */
    public static List<Movie> getGeneralRecommendations() {
        return generalRecommendations;
    }

    /**
     * Refreshes generalRecommendations list
     *
     * @return true if changed false if same
     */
    public static boolean refreshGeneralRecommendations() {
        List<Movie> newGeneralRecommendations = ReviewController.getRecommendations();

        /* Check if same list */
        if (newGeneralRecommendations.size() == generalRecommendations.size()) {
            for (Movie movie : newGeneralRecommendations) {
                if (!generalRecommendations.contains(movie)) {
                    generalRecommendations = newGeneralRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        } else {
            generalRecommendations = newGeneralRecommendations;
            return true;
        }

    }


    /**
     * Getter for major recommendations list
     *
     * @return majorRecommendations list
     */
    public static List<Movie> getMajorRecommendations() {
        if (majorRecommendations == null) {
            refreshMajorRecommendations();
        }
        return majorRecommendations;
    }


    /**
     * Getter for genre recommendations list
     *
     * @param genre Genre to search
     * @return genreRecommendations list
     */
    public static List<Movie> getGenreRecommendations(Genre genre) {
        if (genreRecommendations == null) {
            refreshGenreRecommendations(genre);
        }
        return genreRecommendations;
    }


    /**
     * Getter for rating recommendations list
     *
     * @param rating Rating to search
     * @return ratingRecommendations list
     */
    public static List<Movie> getRatingRecommendations(Rating rating) {
        if (ratingRecommendations == null) {
            refreshRatingRecommendations(rating);
        }
        return ratingRecommendations;
    }


    /**
     * Getter for major genre recommendations list
     *
     * @param genre Genre to search
     * @return majorGenreRecommendations list
     */
    public static List<Movie> getMajorGenreRecommendations(Genre genre) {
        if (majorGenreRecommendations == null) {
            refreshMajorGenreRecommendations(genre);
        }
        return majorGenreRecommendations;
    }


    /**
     * Getter for major rating recommendations list
     *
     * @param rating Rating to search
     * @return majorRatingRecommendations list
     */
    public static List<Movie> getMajorRatingRecommendations(Rating rating) {
        if (majorRatingRecommendations == null) {
            refreshMajorRatingRecommendations(rating);
        }
        return majorRatingRecommendations;
    }


    /**
     * Getter for genre rating recommendations list
     *
     * @param genre genre to search for
     * @param rating rating to search for
     * @return genreRatingRecommendations list
     */
    public static List<Movie> getGenreRatingRecommendations(Genre genre, Rating rating) {
        if (genreRatingRecommendations == null) {
            refreshGenreRatingRecommendations(genre, rating);
        }
        return genreRatingRecommendations;
    }


    /**
     * Getter for major genre rating recommendations list
     *
     * @param genre Genre to search
     * @param rating Rating to search
     * @return majorGenreRatingRecommendations list
     */
    public static List<Movie> getMajorGenreRatingRecommendations(Genre genre, Rating rating) {
        if (majorGenreRatingRecommendations == null) {
            refreshMajorGenreRatingRecommendations(genre, rating);
        }
        return majorGenreRatingRecommendations;
    }

    /**
     * Refreshes major Recommendations list
     *
     * @return true if changed false if same
     */
    public static boolean refreshMajorRecommendations() {
        List<Movie> newMajorRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_MAJOR, null, null);

        /* Check if same list */
        if (majorRecommendations == null
                || newMajorRecommendations == null
                || newMajorRecommendations.size() != majorRecommendations.size()) {
            majorRecommendations = newMajorRecommendations;
            return true;
        } else {
            for (Movie movie : newMajorRecommendations) {
                if (!majorRecommendations.contains(movie)) {
                    majorRecommendations = newMajorRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes genre Recommendations list
     *
     * @param genre Genre to search
     * @return true if changed false if same
     */
    public static boolean refreshGenreRecommendations(Genre genre) {
        List<Movie> newGenreRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_GENRE, genre, null);

        /* Check if same list */
        if (genreRecommendations == null
                || newGenreRecommendations == null
                || newGenreRecommendations.size() != genreRecommendations.size()) {
            genreRecommendations = newGenreRecommendations;
            return true;
        } else {
            for (Movie movie : newGenreRecommendations) {
                if (!genreRecommendations.contains(movie)) {
                    genreRecommendations = newGenreRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes rating Recommendations list
     *
     * @param rating Rating to search
     * @return true if changed false if same
     */
    public static boolean refreshRatingRecommendations(Rating rating) {
        List<Movie> newRatingRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_RATING, null, rating);

        /* Check if same list */
        if (ratingRecommendations == null
                || newRatingRecommendations == null
                || newRatingRecommendations.size() != ratingRecommendations.size()) {
            ratingRecommendations = newRatingRecommendations;
            return true;
        } else {
            for (Movie movie : newRatingRecommendations) {
                if (!ratingRecommendations.contains(movie)) {
                    ratingRecommendations = newRatingRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes major genre Recommendations list
     *
     * @param genre Genre to search
     * @return true if changed false if same
     */
    public static boolean refreshMajorGenreRecommendations(Genre genre) {
        List<Movie> newMajorGenreRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_MAJOR_GENRE, genre, null);

        /* Check if same list */
        if (majorGenreRecommendations == null
                || newMajorGenreRecommendations == null
                || newMajorGenreRecommendations.size() != majorGenreRecommendations.size()) {
            majorGenreRecommendations = newMajorGenreRecommendations;
            return true;
        } else {
            for (Movie movie : newMajorGenreRecommendations) {
                if (!majorGenreRecommendations.contains(movie)) {
                    majorGenreRecommendations = newMajorGenreRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes major rating Recommendations list
     *
     * @param rating Rating to search
     * @return true if changed false if same
     */
    public static boolean refreshMajorRatingRecommendations(Rating rating) {
        List<Movie> newMajorRatingRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_MAJOR_RATING, null, rating);

        /* Check if same list */
        if (majorRatingRecommendations == null
                || newMajorRatingRecommendations == null
                || newMajorRatingRecommendations.size() != majorRatingRecommendations.size()) {
            majorRatingRecommendations = newMajorRatingRecommendations;
            return true;
        } else {
            for (Movie movie : newMajorRatingRecommendations) {
                if (!majorGenreRecommendations.contains(movie)) {
                    majorGenreRecommendations = newMajorRatingRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes genre rating Recommendations list
     *
     * @param genre Genre to search
     * @param rating Rating to search
     * @return true if changed false if same
     */
    public static boolean refreshGenreRatingRecommendations(Genre genre, Rating rating) {
        List<Movie> newGenreRatingRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_GENRE_RATING, genre, rating);

        /* Check if same list */
        if (genreRatingRecommendations == null
                || newGenreRatingRecommendations == null
                || newGenreRatingRecommendations.size() != genreRatingRecommendations.size()) {
            genreRatingRecommendations = newGenreRatingRecommendations;
            return true;
        } else {
            for (Movie movie : newGenreRatingRecommendations) {
                if (!genreRatingRecommendations.contains(movie)) {
                    genreRatingRecommendations = newGenreRatingRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }

    /**
     * Refreshes major genre rating Recommendations list
     *
     * @param genre Genre to search
     * @param rating Rating to search
     * @return true if changed false if same
     */
    public static boolean refreshMajorGenreRatingRecommendations(Genre genre, Rating rating) {
        List<Movie> newMajorGenreRatingRecommendations = ReviewController.getRecommendations(
                ReviewController.BY_MAJOR_GENRE_RATING, genre, rating);

        /* Check if same list */
        if (majorGenreRatingRecommendations == null
                || newMajorGenreRatingRecommendations == null
                || newMajorGenreRatingRecommendations.size() != majorGenreRatingRecommendations.size()) {
            majorGenreRatingRecommendations = newMajorGenreRatingRecommendations;
            return true;
        } else {
            for (Movie movie : newMajorGenreRatingRecommendations) {
                if (!majorGenreRatingRecommendations.contains(movie)) {
                    majorGenreRatingRecommendations = newMajorGenreRatingRecommendations;
                    return true;
                }
            }

            //Exact same list
            return false;
        }

    }


}
