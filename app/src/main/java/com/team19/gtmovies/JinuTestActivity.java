package com.team19.gtmovies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        TomatoMagic magically = new TomatoMagic(getApplicationContext(), TomatoMagic.newMovie);
        try {
            movies = magically.getRawObj().getJSONArray("movies");
        } catch (JSONException e) {
            Log.e("JSON ERROR", "Couldn't get movies from JSON in Test Activity");
        }
        try {
            numOfMovies = movies.length();
            for (int i = 0; i < movies.length(); i++) {
                movieIDs[i] = movies.getJSONObject(i).getInt("id");
            }
        } catch (JSONException e) {
            Log.e("JSON ERROR", "Couldn't break up JSONArray into individual movies");
        }
        if (movies != null && movies.length() != 0) {
            index = 0;
        } else {
            Log.e("TestActivity Error", "movies not intialized");
        }

        Movie tmp = null;
        try {
            tmp = new Movie(movies.getJSONObject(index));
        } catch (JSONException e) {
            Log.e("Movie Error", "Couldn't create a Movie object");
        }

        movieTitle.setText(tmp.getTitle());
        movieID.setText(tmp.getID());

        // ORIGINAL INITIATION HERE
//        new JSONParse().execute();

//        final JSONParse parse = new JSONParse();
//        parse.execute();
        // do it so that every time it executes it goes to next movie

        nextButton = (Button) findViewById(R.id.nextMovieButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie tmp2 = null;
                index %= numOfMovies;
                try {
                    tmp2 = new Movie(movies.getJSONObject(index));
                } catch (JSONException e) {
                    Log.e("Movie Error", "Couldn't create a Movie object");
                }

                movieTitle.setText(tmp2.getTitle());
                movieID.setText(tmp2.getID());
            }
        });
    }



}
