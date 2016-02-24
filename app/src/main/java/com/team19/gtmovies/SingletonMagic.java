package com.team19.gtmovies;

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

/**
 * Created by Jim Jang on 2016-02-22.
 */
public class SingletonMagic {
    // Basic URL for the Tomato API
    // Has three %s placeholders in the middle of baseURL
    public static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?%sapikey=%s";
    public static String boxOffice = "/lists/movies/box_office";
    public static String newMovie = "/lists/movies/opening";
    public static String newDVD = "/lists/dvds/new_releases";
    public static String topRental = "/lists/dvds/top_rentals";
    public static String search = "/movies";

    //Setting strings for URL
    public static String numMovies = "limit=%d&";
    public static String country = "country=%s&";
    public static String perPage = "page_limit=%d";
    public static String numPage = "page=%d";
    public static String profKey = "yedukp76ffytfuy24zsqk7f5";

    private static SingletonMagic ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private JSONObject rawObj;
    public boolean nextable = true;
    public boolean prevable = false;
    private String nextURL = null;
    private String prevURL = null;

    // Copied from Android Volley site
    private SingletonMagic(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    // Default fx on creation
//    public static SingletonMagic getInstance() {
//        return ourInstance;
//    }

    // Copied from Android Volley site
    public static synchronized SingletonMagic getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SingletonMagic(context);
        }
        return ourInstance;
    }

    // Copied from Android Volley site
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    // TODO: Erase everything below at end of M5
    // Below are all deprecated
    // Will erase at end of M5
    public void request(String target) {
        if (!target.equals(boxOffice)
                && !target.equals(newMovie)
                && !target.equals(newDVD)) {
            throw new IllegalArgumentException("Request only uses set options");
        }
        String urlRaw = String.format(baseURL, target, "", profKey);
        JsonObjectRequest jObjReq = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        Log.e("WAHAHAHAHA", "TO HELL WITH THE WORLD!!!!");
//                        //handle a valid response coming back.  Getting this string mainly for debug
//                        response = resp.toString();
//                        //printing first 500 chars of the response.  Only want to do this for debug
//                        TextView view = (TextView) findViewById(R.id.textView2);
//                        view.setText(response.substring(0, 500));
                        if (resp == null) {
                            Log.e("FSDIADFNIFANI", "TO HELL WITH THE WORLD!!!!");
                        }

                        // Parsing of the info
                        rawObj = resp;
                        // first checking if this is a Page using API
                        try {
                            nextURL = rawObj.getJSONObject("links").getString("next");
                            Log.e("HELL!!", nextURL.toString());
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when magically getting nextURL");
                        }
                        if (nextURL == null) {
                            nextable = false;
                        }

                        // put movies into a JSONArray
//                        try {
//                            movies = rawObj.getJSONArray("movies");
//                        } catch (JSONException e) {
//                            Log.e("JSON ERROR", "Error when magically getting movies");
//                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Coudln't getJSON");
                    }
                });
        Log.e("!!!!!!!!!!", "About to add to queue!!!");
        mRequestQueue.add(jObjReq);
        Log.e("!!!!!!!!!!", "Added to queue!!!");
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

//                        // put movies into a JSONArray
//                        try {
//                            movies = rawObj.getJSONArray("movies");
//                        } catch (JSONException e) {
//                            Log.e("JSON ERROR", "Error when getting movies from URL");
//                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });
        mRequestQueue.add(jObjReq);
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
     * Returns the JSONObject of the current page
     *
     * @return JSONObject of current page
     */
    public JSONObject getRawObj() {
        return rawObj;
    }
}
