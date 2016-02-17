package com.team19.gtmovies;


import android.util.Log;

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

/**
 * I insist the 'ma' be pronounced as mɑː
 * Created by Jim Jang on 2016-02-16.
 */
public class TomatoParser {
    static JSONObject thing;

    /**
     * Grabs the JSON from given URL
     * Just returns null if null URL is passed
     *
     * @param url the target URL to grab the JSON from
     * @return the grabbed JSON, null if null URL passed
     */
    public JSONObject getJSON(URL url) {
        // Initial connection opened
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
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
        }

        // Actually reading to get raw String for JSON
        String jsonRaw = "";
        BufferedReader br = null;
        try {
            String currentLine;
            br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((currentLine = br.readLine()) != null) {
                jsonRaw += currentLine;
                jsonRaw += "\n";
            }
        } catch (Exception e) {
            Log.e("Buffer Error", "JSON Reading Failure: Buffer go BOOM!!" + e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                Log.e("Buffer Error", "Couldn't close buffer" + e.toString());
            }
        }

        // Time to actually make that JSON
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(jsonRaw);
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception thrown. I don't even..." + e.toString());
        }

        return jObj;
    }

    /**
     * Grabs the JSON from given String
     *
     * @param urlStr the target URL in a String format
     * @return the grabbed JSON, null if incorrect String passed
     */
    public JSONObject getJSON(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getJSON(url);
    }
}
