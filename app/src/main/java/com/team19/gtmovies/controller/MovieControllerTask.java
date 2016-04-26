package com.team19.gtmovies.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.abstractClasses.MovieControlActivity;
import com.team19.gtmovies.data.PageData;
import com.team19.gtmovies.data.SingletonMagic;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Genre;
import com.team19.gtmovies.pojo.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Austin Leal on 4/20/2016.
 *
 * @author Austin Leal
 * @version 1.0
 */
public class MovieControllerTask extends AsyncTask<Object, Void, Void> {
    public static final int GET_ALL_MOVIES = 0;
    public static final int UPDATE_NEW_MOVIES = 1;
    public static final int MORE_NEW_MOVIES = 2;
    public static final int UPDATE_RENTALS = 3;
    public static final int MORE_RENTALS = 4;
    public static final int UPDATE_RECOMMENDATIONS = 5;
    public static final int MORE_RECOMMENDATIONS = 6;
    public static final int SEARCH_MOVIES = 7;
    public static final int MORE_SEARCH_MOVIES = 8;
    public static final String RECOMMENDATIONS_GENERAL = "general";
    public static final String RECOMMENDATIONS_BY_MAJOR = "by major";
    public static final String RECOMMENDATIONS_BY_GENRE = "by genre";
    public static final String RECOMMENDATIONS_BY_RATING = "by rating";
    public static final String RECOMMENDATIONS_BY_MAJOR_GENRE = "by major genre";
    public static final String RECOMMENDATIONS_BY_MAJOR_RATING = "by major rating";
    public static final String RECOMMENDATIONS_BY_GENRE_RATING = "by genre rating";
    public static final String RECOMMENDATIONS_BY_MAJOR_GENRE_RATING = "by major genre rating";

    private static final int PAGE_SIZE = 20;

    private MovieControlActivity currentActivity;
    private int requestType;
    private String otherCode;
    private Object otherData1;
    private Object otherData2;
    private int responsesOut;
    private int currentRecommendation;
    private int maxRecommendation;
    private List<Movie> recommendationsList;
    private Map<Integer, Movie> recommendationsMap;

    private boolean visited[];

    @Override
    protected Void doInBackground(Object... params) {
        currentActivity = (MovieControlActivity) params[0];
        requestType = (int) params[1];
        otherCode = (String) params[2];
        otherData1 = params[3];
        otherData2 = params[4];

        responsesOut = 0;
        recommendationsMap = null;
        getMoviesFromAPI();
        return null;
    }

    private void getMoviesFromAPI() {
        visited = new boolean[3];
        for (int i = 0; i < 3; i++) {
            visited[0] = false;
        }

        if (requestType == GET_ALL_MOVIES) {
            getFirstTwoTabsFromAPI(SingletonMagic.NEW_MOVIE, "");
            getFirstTwoTabsFromAPI(SingletonMagic.TOP_RENTAL, "");
            otherCode = RECOMMENDATIONS_GENERAL;
            getRecommendations();
        } else if (requestType == SEARCH_MOVIES) {
            getFirstTwoTabsFromAPI(SingletonMagic.SEARCH, otherCode);
        } else if (requestType == UPDATE_NEW_MOVIES) {
            //PageData.
            getFirstTwoTabsFromAPI(SingletonMagic.NEW_MOVIE, "");
        } else if (requestType == UPDATE_RENTALS) {
            getFirstTwoTabsFromAPI(SingletonMagic.TOP_RENTAL, "");
        } else if (requestType == UPDATE_RECOMMENDATIONS) {
            getRecommendations();
        }
    }

    private void getRecommendations() {
        recommendationsMap = new HashMap<>();
        recommendationsList = null;
        if (otherCode.equals(RECOMMENDATIONS_GENERAL)) {
            recommendationsList = PageData.getGeneralRecommendations();
        } else if (otherCode.equals(RECOMMENDATIONS_BY_MAJOR)) {
            recommendationsList = PageData.getMajorRecommendations();
        } else if (otherCode.equals(RECOMMENDATIONS_BY_GENRE)) {
            recommendationsList = PageData.getGenreRecommendations((Genre) otherData1);
        } else if (otherCode.equals(RECOMMENDATIONS_BY_RATING)) {
            recommendationsList = PageData.getRatingRecommendations((Rating) otherData1);
        } else if (otherCode.equals(RECOMMENDATIONS_BY_MAJOR_GENRE)) {
            recommendationsList = PageData.getMajorGenreRecommendations((Genre) otherData1);
        } else if (otherCode.equals(RECOMMENDATIONS_BY_MAJOR_RATING)) {
            recommendationsList = PageData.getMajorRatingRecommendations((Rating) otherData1);
        } else if (otherCode.equals(RECOMMENDATIONS_BY_GENRE_RATING)) {
            recommendationsList = PageData.getGenreRatingRecommendations((Genre) otherData1,
                    (Rating) otherData2);
        } else if (otherCode.equals(RECOMMENDATIONS_BY_MAJOR_GENRE_RATING)) {
            recommendationsList = PageData.getMajorGenreRatingRecommendations((Genre) otherData1,
                    (Rating) otherData2);
        }
        if (recommendationsList != null && recommendationsList.size() > 0) {
            maxRecommendation = (PAGE_SIZE < recommendationsList.size())
                    ?  PAGE_SIZE : recommendationsList.size();
            Log.d("MovieController", "PAGE_SIZE=" + PAGE_SIZE + " recSize="
                    + recommendationsList.size() + " max=" + maxRecommendation);
            currentRecommendation = 0;
            cycleRecommendations();
        }
    }

    /**
     * Obtains all the movies from the API
     *
     */
    private void getRecommendationFromAPI(Movie movie) {
        // Creating the JSONRequest
        responsesOut++;
        JsonObjectRequest movieRequest = null;
        Log.d("getMoviesFromAPI", "recommendations");
        if (movie == null) {
            return;
        }

        //create the request
        String movieID = SingletonMagic.SEARCH + "/" + movie.getID();
        final String urlRaw = String.format(
                SingletonMagic.BASE_URL, movieID, "", SingletonMagic.PROF_KEY);
        movieRequest = new JsonObjectRequest(Request.Method.GET,
                urlRaw, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                if (resp == null) {
                    Log.e("JSONRequest ERROR", "getMovie Null Response Received");
                }

                Movie mMovie = new Movie(resp);
                recommendationsMap.put(mMovie.getID(), mMovie);

                Log.d("getMovie movie success", "rec movie:" + new Movie(resp));
                Log.d("getMovie volley success", "rec movie:" + urlRaw);
                responsesOut--;
                cycleRecommendations();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responsesOut--;
                cycleRecommendations();
                Log.e("getMovie VOLLEY FAIL", "Couldn't getJSON. rec movie:" + urlRaw);
            }
        });
        SingletonMagic.getInstance(currentActivity).addToRequestQueue(movieRequest);
        cycleRecommendations();
    }




    private void getFirstTwoTabsFromAPI(final String requestCode, final String extra) {
        responsesOut++;
        JsonObjectRequest movieRequest = null;
        final List<Movie> movieArray = new ArrayList<>();
        final String urlRaw = String.format(
                SingletonMagic.BASE_URL, requestCode, extra, SingletonMagic.PROF_KEY);

        movieRequest = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                if (resp == null) {
                    Log.e("JSONRequest ERROR", "Null Response Received");
                }
                // put movies into a JSONArray
                JSONArray tmpMovies = null;
                try {
                    tmpMovies = resp.getJSONArray("movies");
                } catch (JSONException e) {
                    Log.e("JSON ERROR", "Error when getting movies in Main.");
                    return;
                }
                if (tmpMovies == null) {
                    Log.e("Movie Error", "movies JSONArray is null!");
                    return;
                }
                for (int i = 0; i < tmpMovies.length(); i++) {
                    try {
                        movieArray.add(new Movie(tmpMovies.getJSONObject(i)));
                    } catch (JSONException e) {
                        Log.e("Movie Error", "Couldn't make Movie" + i + "in Main");
                    }
                }

                Log.d("MovieController", "response");

                //Create proper MovieListFragment
                if (requestCode.equals(SingletonMagic.NEW_MOVIE)) {
                    Log.d("JinuMain", "newMovieFragment");
                    MovieListFragment.setNewMoviesList(movieArray);
                    MovieListFragment.updateAdapter(MovieListFragment.NEW_MOVIES_TAB);
                } else if (requestCode.equals(SingletonMagic.TOP_RENTAL)) {
                    MovieListFragment.setTopRentalsList(movieArray);
                    MovieListFragment.updateAdapter(MovieListFragment.TOP_RENTALS_TAB);
                } else if (requestCode.equals(SingletonMagic.SEARCH)) {
                    Log.d("MovieController", "search response");
                    MovieListFragment.setSearchMovieList(movieArray);
                    MovieListFragment.updateAdapter(MovieListFragment.SEARCH);
                } else {
                    Log.d("JinuMain", "nullFragment");
                    //movieListFragment = null;
                }
                markVisited(requestCode);
                responsesOut--;
                cycleRecommendations();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                markVisited(requestCode);
                responsesOut--;
                cycleRecommendations();
                Log.e("VOLLEY FAIL", "Couldn't getJSON");
            }
        });
        SingletonMagic.getInstance(currentActivity).addToRequestQueue(movieRequest);
    }








    private void markVisited(String requestCode) {
        if (requestCode == SingletonMagic.NEW_MOVIE) {
            visited[0] = true;
        } else if (requestCode == SingletonMagic.TOP_RENTAL) {
            visited[1] = true;
        }
    }

    private void cycleRecommendations() {
        Log.d("MovieController", "out=" + responsesOut);
        if (recommendationsList != null && responsesOut <= 5 && currentRecommendation < maxRecommendation) {
            Log.d("MovieController", "get more" + currentRecommendation + " max=" + maxRecommendation);
            getRecommendationFromAPI(recommendationsList.get(currentRecommendation++));
        } else if (currentRecommendation >= (maxRecommendation - 1) && responsesOut == 0) {
            finishRecommendations();
        }
    }



    private void finishRecommendations() {
        if (requestType == MORE_RECOMMENDATIONS) {
            //add to current lists
            Log.d("MovieController", "called more_recs");
        } else {
            Log.d("MovieController", "Should be calling this");
            MovieListFragment.setYourRecommendationsList(recommendationsList);
            MovieListFragment.updateAdapter(MovieListFragment.YOUR_RECOMMENDATIONS_TAB);
            visited[2] = true;
        }
        Log.d("MovieController", "called finish recs");
        finishRequest();
    }

    private void finishRequest() {
        Log.d("MovieController", "finish");
        if (requestType == GET_ALL_MOVIES) {
            for (boolean visit : visited) {
                if (!visit) {
                    Log.d("MovieController", "Not finished visited");
                    return;
                }
            }
            Log.d("MovieController", "finish all visited");
            Log.d("MovieController", "Recs=" + MovieListFragment.getYourRecommendationsList());
            currentActivity.finishedGettingMovies(requestType);
        } else {
            Log.d("MovieController", "just finish ");
            currentActivity.finishedGettingMovies(requestType);
        }
    }
}
