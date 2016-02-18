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

/**
 * The AsyncTask to get that Movie view set-up
 * This is a test prototype so it's currently stupid
 * Can be changed to use Strings in doInBackground
 *
 */
private class JSONParse extends AsyncTask<String, Integer, JSONObject> {
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
    protected JSONObject doInBackground(String... urlParts) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        String jsonRaw = "";
        BufferedReader br = null;
        String urlRaw = String.format(
                baseURL, boxOffice, "limit="+numOfMovies+"&country=us&", profKey);
        URL url = null;
        try {
            url = new URL(urlRaw);
        } catch (MalformedURLException e) {
            Log.e("URL ERROR", "Failed to create URL in background");
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            String currentLine = null;
            br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8), 8);
            while ((currentLine = br.readLine()) != null) {
                jsonRaw += currentLine;
                jsonRaw += "\n";
            }
        } catch (IOException e) {
            Log.e("HTTP Connection Error", "HTTP Connection Failure" + e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            } else {
                ///////////////////////////////////////////
                // No Internet Connection Pop-Up or smth //
                // Program later                         //
                ///////////////////////////////////////////
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                Log.e("Buffer Error", "Couldn't close buffer" + e.toString());
            }
        }

        JSONObject tmpJSON = null;
        try {
            tmpJSON = new JSONObject(jsonRaw);
        } catch (JSONException e) {
            Log.e("JSON Error", "Couldn't make tmpJSON" + e.toString());
        }
        try {
            movies = tmpJSON.getJSONArray("movies");
        } catch (JSONException e) {
            Log.e("JSON Error", "Exception while initial movie read in" + e.toString());
        }
        if (movies != null) {
            index = 0;
        }

        JSONObject movieJSON = null;
        try {
            movieJSON = movies.getJSONObject(index++);
        } catch (JSONException e) {
            Log.e("JSON Error", "Exception while initial MovieView set up" + e.toString());
        }
        return movieJSON;
    }

//    @Override
//    protected JSONObject doInBackground(String... urlParts) {
//        JSONObject movieJSON = null;
//
//        // Just in case index becomes static later
//        if (index == -1) {
//            try {
//                movies = new TomatoParser().getJSON(
//                        String.format(
//                                baseURL, boxOffice, "limit="+numOfMovies+"&country=us&", profKey)).
//                        getJSONArray("movies");
//            } catch (JSONException e) {
//                Log.e("JSON Error", "Exception while initial movie readin" + e.toString());
//            }
//            if (movies != null) {
//                index = 0;
//            }
//        }
//        try {
//            movieJSON = movies.getJSONObject(index++);
//        } catch (JSONException e) {
//            Log.e("JSON Error", "Exception while initial MovieView set up" + e.toString());
//        }
//        return movieJSON;
//    }

    @Override
    protected void onPostExecute(JSONObject movieJSON) {
        Movie movie = new Movie(movieJSON);
        pDialog.dismiss();
        movieTitle.setText(movie.getTitle());
        movieID.setText(Integer.toString(movie.getID()));
        String tmpGenres = "";
        for (Genre g : movie.getGenres()) {
            tmpGenres += g;
            tmpGenres += ", ";
        }
        movieGenre.setText(tmpGenres);
    }
}

}
