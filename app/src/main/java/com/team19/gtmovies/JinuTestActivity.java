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

import java.net.URL;

public class JinuTestActivity extends AppCompatActivity {

    private ImageView moviePic;
    private TextView movieTitle;
    private TextView movieID;
    private TextView movieGenre;
    private Button nextButton;

    //For storing the Movies and going through them
    private JSONArray movies = null;
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
        // Just in case index becomes static later
        if (index == -1) {
            try {
                movies = new TomatoParser().getJSON(
                        String.format(
                                baseURL, boxOffice, "limit="+numOfMovies+"&country=us&", profKey)).
                        getJSONArray("movies");
            } catch (JSONException e) {
                Log.e("JSON Error", "Exception while initial movie readin" + e.toString());
            }
            if (movies != null) {
                index = 0;
            }
        }
        try {
            new JSONParse().execute(movies.getJSONObject(index++));
        } catch (JSONException e) {
            Log.e("JSON Error", "Exception while initial MovieView set up" + e.toString());
        }
//        final JSONParse parse = new JSONParse();
//        parse.execute();
        // do it so that every time it executes it goes to next movie

        nextButton = (Button) findViewById(R.id.nextMovieButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    index %= numOfMovies;
                    new JSONParse().execute(movies.getJSONObject(index++));
                } catch (JSONException e) {
                    Log.e("JSON Error", "Exception while NextButton click" + e.toString());
                }
            }
        });
    }

/**
 * The AsyncTask to get that Movie view set-up
 * This is a test prototype so it's currently stupid
 * Receives JSONObject, makes Movie out of it, updates appropriate Views.
 *
 */
private class JSONParse extends AsyncTask<JSONObject, Integer, Movie> {
    private ProgressDialog pDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        moviePic = (ImageView)findViewById(R.id.imageView2);
        movieTitle = (TextView)findViewById(R.id.titleView);
        movieID = (TextView)findViewById(R.id.idView);
        movieGenre = (TextView)findViewById(R.id.genreView);
        pDialog = new ProgressDialog(JinuTestActivity.this);
        pDialog.setMessage("Pulling movie...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    @Override
    protected Movie doInBackground(JSONObject... movie) {
        return new Movie(movie[0]);
    }

    @Override
    protected void onPostExecute(Movie movie) {
        pDialog.dismiss();
        movieTitle.setText(movie.getTitle());
        movieID.setText(movie.getID());
        String tmpGenres = "";
        for (Genre g : movie.getGenres()) {
            tmpGenres += g;
            tmpGenres += ", ";
        }
        movieGenre.setText(tmpGenres);
    }
}

}
