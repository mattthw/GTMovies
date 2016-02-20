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
    private static int numOfMovies = 16;
    private static String profKey = "yedukp76ffytfuy24zsqk7f5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinu_test);

        // ORIGINAL INITIATION HERE
        new JSONParse().execute();

//        final JSONParse parse = new JSONParse();
//        parse.execute();
        // do it so that every time it executes it goes to next movie

        nextButton = (Button) findViewById(R.id.nextMovieButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index %= numOfMovies;
                new JSONParse().execute();
            }
        });
    }



}
