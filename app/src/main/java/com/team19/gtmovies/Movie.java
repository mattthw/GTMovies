package com.team19.gtmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jim Jang on 2016-02-17.
 */
public class Movie implements Comparable<Movie>{
    // Remembers immediate things
    // Rest in JSON
    int id;
    String title;
    ArrayList<Genre> genres = new ArrayList<>();
    JSONObject fullInfo;
    static String base =
            "http://api.rottentomatoes.com/api/public/v1.0/movies/%d.json?apikey=%s";

    /**
     * Receives the movie id, and the API key and creates the Movie object.
     *
     * @param id the id of the Movie
     * @param apiKey the API key to access the Rotten Tomatoes API
     */
    public Movie(int id, String apiKey) {
        this(String.format(base, id, apiKey));
    }

    /**
     * Receives the full String of the URL address and creates the Movie object from it.
     *
     * @param urlStr String of the target URL
     */
    public Movie(String urlStr) {
        TomatoParser tomato = new TomatoParser();
        fullInfo = tomato.getJSON(urlStr);
        JSONArray tmpJArray;
        try {
            id = fullInfo.getInt("id");
            title = fullInfo.getString("title");
            tmpJArray = fullInfo.getJSONArray("genres");
            for (int i = 0; i < tmpJArray.length(); i++) {
                genres.add(Genre.toGenre(tmpJArray.getString(i)));
            }
        } catch (JSONException e) {
            Log.e("JSON Error", "JSONException while parsing single movie" + e.toString());
        }
    }

    @Override
    public int compareTo(Movie other) {
        return this.id - other.getID();
    }

    /**
     * Returns the id of the Movie
     *
     * @return the int id of the Movie
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the title of the Movie
     *
     * @return the title of the Movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the Genres of the Movie as an ArrayList
     *
     * @return the Genres of the Movie as an ArrayList
     */
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     * Returns backing JSONObject of this Movie
     *
     * @return the backing JSONObject of this Movie
     */
    public JSONObject getFullInfo() {
        return fullInfo;
    }
}
