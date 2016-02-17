package com.team19.gtmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import java.net.URL;

public class JinuTestActivity extends AppCompatActivity {

    // Basic URL for the Tomato API
    // Has two %s placeholders in the middle of baseURL
    private static String baseURL =
            "http://api.rottentomatoes.com/api/public/v1.0%s.json?apikey=%s";
    private static String profKey = "yedukp76ffytfuy24zsqk7f5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinu_test);
    }

// I'm going to store my Async here
// -Jinu

/**
 * The AsyncTask to get that JSON
 * Currently can just uses the first URL
 *
 * Originally meant to be var-args URL... url
 */
private class JSONParse extends AsyncTask<URL, Integer, JSONObject> {
    @Override
    protected JSONObject doInBackground(URL... url) {
        TomatoParser tomato = new TomatoParser();

        JSONObject jObj = tomato.getJSON(url[0]);
        return jObj;
    }

    protected  JSONObject doInBackground(String str) {
        TomatoParser tomato = new TomatoParser();

        JSONObject jObj = tomato.getJSON(str);
        return jObj;
    }
}

}
