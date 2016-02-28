package com.team19.gtmovies;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jim Jang on 2016-02-22.
 */
public class SingletonMagic {
    // Basic URL for the Tomato API
    // Has three %s placeholders in the middle of baseURL
    public static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?%s&apikey=%s";
    public static String boxOffice = "/lists/movies/box_office";
    public static String newMovie = "/lists/movies/opening";
    public static String newDVD = "/lists/dvds/new_releases";
    public static String topRental = "/lists/dvds/top_rentals";
    public static String search = "/movies";

    //Setting strings for URL
    public static String numMovies = "limit=%d";
    public static String country = "country=%s";
    public static String perPage = "page_limit=%d";
    public static String numPage = "page=%d";
    public static String profKey = "yedukp76ffytfuy24zsqk7f5";

    private static SingletonMagic ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    /**
     * Constructor for Singleton. Takes the Application Context as parameter
     * @param context Context of the Singleton. Recommended to be ApplicationContext
     */
    private SingletonMagic(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Returns the instance for the Singleton
     * Creates a new one if instance does not exist
     * @param context Context of the Singleton. Recommended to be ApplicationContext
     * @return the instance of the Singleton
     */
    public static synchronized SingletonMagic getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SingletonMagic(context);
        }
        return ourInstance;
    }

    /**
     * Returns the Singleton RequestQueue
     * @return the Singleton RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds a Volley.Request to the RequestQueue
     * @param req the Request to add
     * @param <T> the expected response type of the Request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
