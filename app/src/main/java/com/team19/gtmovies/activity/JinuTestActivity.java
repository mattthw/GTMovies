package com.team19.gtmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.R;
import com.team19.gtmovies.SingletonMagic;
import com.team19.gtmovies.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.team19.gtmovies.R;
import com.team19.gtmovies.SingletonMagic;
import com.team19.gtmovies.pojo.Movie;

public class JinuTestActivity extends AppCompatActivity {

    private ImageView moviePic;
    private TextView movieTitle;
    private TextView movieID;
    private TextView movieGenre;
    private Button nextButton;

    //For storing the Movies and going through them
    private JSONArray movies = null;
    private int[] movieIDs;
    private int index = -1;

    // Basic URL for the Tomato API
    // Has two %s placeholders in the middle of baseURL
    private static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?%sapikey=%s";
    private static String boxOffice = "/lists/movies/box_office";
    private int numOfMovies = 16;
    private static String profKey = "yedukp76ffytfuy24zsqk7f5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinu_test);
        movieTitle = (TextView) findViewById(R.id.titleView);
        movieID = (TextView) findViewById(R.id.idView);

        String urlRaw = String.format(baseURL, "/lists/movies/opening", "", profKey);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
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

                        // put movies into a JSONArray
                        try {
                            movies = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies in test acti.");
                        }
                        if (movies == null) {
                            Log.e("Movie Error", "movies JSONArray is null!");
                        }
                        Movie tmp = null;
                        try {
                            tmp = new Movie(movies.getJSONObject(++index));
                        } catch (JSONException e) {
                            Log.e("Movie Error", "Couldn't initially create a Movie obj");
                        }
                        if (tmp == null) {
                            Log.e("Movie Error", "No JSONError but Movie is null");
                        }
                        movieTitle.setText(tmp.getTitle());
                        movieID.setText(Integer.toString(tmp.getID()));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Coudln't getJSON");
                    }
                });

// Access the RequestQueue through your singleton class.
        SingletonMagic.getInstance(this).addToRequestQueue(jsObjRequest);


        boolean following_is_not_using_add_as_request;
//        SingletonMagic magically = SingletonMagic.getInstance(getApplicationContext());
//        magically.request(SingletonMagic.newMovie);
//        JSONObject rawObj = magically.getRawObj();
//        try {
//            movies = rawObj.getJSONArray("movies");
//        } catch (JSONException e) {
//            Log.e("JSON Error", "Couldn't get movies JSONArray");
//        }
//        if (movies != null) {
//            index = 0;
//        }
//        Movie tmp = null;
//        try {
//            tmp = new Movie(movies.getJSONObject(index));
//        } catch (JSONException e) {
//            Log.e("JSON Error", "Couldn't get an individual Movie out of the JArray");
//        }

        boolean following_is_using_Tomato_magic;
//        TomatoMagic magically = new TomatoMagic(getApplicationContext(), TomatoMagic.newMovie);
//        try {
//            movies = magically.getRawObj().getJSONArray("movies");
//        } catch (JSONException e) {
//            Log.e("JSON ERROR", "Couldn't get movies from JSON in Test Activity");
//        }
//        try {
//            numOfMovies = movies.length();
//            for (int i = 0; i < movies.length(); i++) {
//                movieIDs[i] = movies.getJSONObject(i).getInt("id");
//            }
//        } catch (JSONException e) {
//            Log.e("JSON ERROR", "Couldn't break up JSONArray into individual movies");
//        }
//        if (movies != null && movies.length() != 0) {
//            index = 0;
//        } else {
//            Log.e("TestActivity Error", "movies not intialized");
//        }
//
//        Movie tmp = null;
//        try {
//            tmp = new Movie(movies.getJSONObject(index));
//        } catch (JSONException e) {
//            Log.e("Movie Error", "Couldn't create a Movie object");
//        }
//
//        movieTitle.setText(tmp.getTitle());
//        movieID.setText(tmp.getID());

        nextButton = (Button) findViewById(R.id.nextMovieButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("FUCK YOU", "I'm Running. Just Not Doing Anything");
                Movie tmp2 = null;
                index += 1;
                index %= numOfMovies;
                try {
                    tmp2 = new Movie(movies.getJSONObject(index));
                } catch (JSONException e) {
                    Log.e("Movie Error", "Couldn't create a Movie object");
                }

                movieTitle.setText(tmp2.getTitle());
                movieID.setText(Integer.toString(tmp2.getID()));
//                Log.d("FUCK YOU", "I'm Running. Just Not Doing Anything");
            }
        });
    }



}
