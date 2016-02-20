package com.team19.gtmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jim Jang on 2016-02-20.
 */
public class TomatoMagic {
    // Basic URL for the Tomato API
    // Has two %s placeholders in the middle of baseURL
    public static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?%sapikey=%s";
    public static String boxOffice = "/lists/movies/box_office";
    public static String newMovie = "/lists/movies/opening";
    public static String newDVD = "/lists/dvds/new_releases";

    //Setting strings for URL
    public static String numMovies = "limit=%d&";
    public static String country = "country=%s&";
    public static String perPage = "page_limit=%d";
    public static String numPage = "page=%d";
    public static String profKey = "yedukp76ffytfuy24zsqk7f5";

    // Actual instance variables
    static RequestQueue queue = null;
    private JSONObject rawObj;
    private JSONArray movies;
    private boolean nextable = true;
    private boolean prevable = false;
    private String nextURL = null;
    private String prevURL = null;

    // Need to find a better way to do stuff
    // Some API results use a friggin Page system in which case the URL becomes completely different

    /**
     * Gets the JSON from given target
     *
     * @param target where to get the JSON from (one of following: boxOffice, newMovie, or newDVD)
     * @throws IllegalArgumentException if wrong options are given
     * @return the JSONObject grabbed from the target
     */
    public JSONObject grabJSONFrom(String target) throws IllegalArgumentException {
        if (!target.equals(boxOffice)
                && !target.equals(newMovie)
                && !target.equals(newDVD)) {
            throw new IllegalArgumentException("GetJSON only uses set options");
        }
        String urlRaw = String.format(baseURL, target, profKey);
        JsonObjectRequest jObjReq = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
//                        //handle a valid response coming back.  Getting this string mainly for debug
//                        response = resp.toString();
//                        //printing first 500 chars of the response.  Only want to do this for debug
//                        TextView view = (TextView) findViewById(R.id.textView2);
//                        view.setText(response.substring(0, 500));

                        // Parsing of the info
                        rawObj = resp;
                        // first checking if this is a Page using API
                        try {
                            nextURL = rawObj.getJSONObject("links").getString("next");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when magically getting nextURL");
                        }
                        if (nextURL == null) {
                            nextable = false;
                        }

                        // put movies into a JSONArray
                        try {
                            movies = rawObj.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when magically getting movies");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Coudln't getJSON");
                    }
                });
        queue.add(jObjReq);
        return rawObj;
    }

    /**
     * Initializes the TomatoMagic
     * Initializes a working RequestQueue if one does not already exist
     * Grabs the JSON from the target and stores it
     * Requires the context of where this is being run in (highly likely just getApplicationContext)
     * Also only takes set options for target:
     *   TomatoMagic.boxOffice
     *   TomatoMagic.newMovie
     *   TomatoMagic.newDVD
     *
     * @param context the context where TomatoMagic is being run in
     * @param target the target to grab the JSON from
     * @throws IllegalArgumentException thrown when wrong target is input
     */
    public TomatoMagic(Context context, String target) throws IllegalArgumentException {
        if (!target.equals(boxOffice)
                && !target.equals(newMovie)
                && !target.equals(newDVD)) {
            throw new IllegalArgumentException("TomatoMagic only uses set options");
        }
        String urlRaw = String.format(baseURL, target, profKey);

        //First things first get Volley up and running
        // Actually don't know if this is necessary: initialzing only when not initialized            // PROBLEMO TO LOOK AT
        // What if context changes???????? Can that happen???? Hopefully it s just always
        // getAppCntxt?
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        grabJSONFrom(target);
    }

    //TODO
    public void grabJSONWithSettings() {
        //
    }

    /**
     * Grabs the JSON from the given URL and saves it as an instance variable
     *
     * @param url the target URL
     */
    private void grabJSONFromURL(String url) {
        JsonObjectRequest jObjReq = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        // Parsing of the info
                        rawObj = resp;
                        // first checking if this is a Page using API
                        try {
                            nextURL = rawObj.getJSONObject("links").getString("next");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error getting nextURL from URL");
                        }
                        if (nextURL == null) {
                            nextable = false;
                        }
                        try {
                            prevURL = rawObj.getJSONObject("links").getString("prev");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error getting prevURL from URL");
                        }
                        if (prevURL == null) {
                            prevable = false;
                        }

                        // put movies into a JSONArray
                        try {
                            movies = rawObj.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies from URL");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });
        queue.add(jObjReq);
    }

    /**
     * Grabs the JSONObject from the next Page
     *
     * @return the JSONObject from the next Page
     * @throws UnsupportedOperationException when no nextPage exists currently
     */
    public JSONObject grabNextPage() throws UnsupportedOperationException {
        if (!nextable) {
            throw new UnsupportedOperationException("No next page found");
        }
        grabJSONFromURL(nextURL);
        return rawObj;
    }

    /**
     * Grabs the JSONObject from the previous Page
     *
     * @return the JSONObject from the previous Page
     * @throws UnsupportedOperationException when no prevPage exists currently
     */
    public JSONObject grabPrevPage() throws UnsupportedOperationException {
        if (!prevable) {
            throw new UnsupportedOperationException("No previous page found");
        }
        grabJSONFromURL(prevURL);
        return rawObj;
    }

    /**
     * Returns the JSONArray of movies currently saved
     *
     * @return JSONArray of movies
     */
    public JSONArray getMovies() {
        return movies;
    }

    /**
     * Returns the JSONObject of the current page
     *
     * @return JSONObject of current page
     */
    public JSONObject getRawObj() {
        return rawObj;
    }
}