package com.team19.gtmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStreamReader;

public class MovieLookUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_look_up);
    }

    /**
     * Created by ericmartin827 on 2/16/16.
     */
    public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            // These teo variables must be decalred outside
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            //Will contain the raw JSON response as a String
            String movieJsonStr = null;

            // The try block attempts to open the connection to Rotten
            // Tomatoes and stores the result of the of the REST Query into
            // a JSON string.
            try {
                String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=16&country=us";
                String apiKey = "&apikey=yedukp76ffytfuy24zsqk7f5";
                URL url = new URL(baseURL.concat(apiKey));


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                //For debugging purposes.
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "movieJsonString:" + movieJsonStr);

            } catch (IOException e) {

                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error Clossing Steam", e);
                    }
                }

            }
            return null;
        }

    }

}
