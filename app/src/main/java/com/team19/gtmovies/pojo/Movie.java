package com.team19.gtmovies.pojo;

import android.media.Image;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jim Jang on 2016-02-17.
 */
public class Movie implements Comparable<Movie> {
    // Remembers immediate things
    // Rest in JSON
    private int id;
    private String title;
    private ArrayList<Genre> genres = new ArrayList<>();
    private String posterURL;
    private int rating = 0;
    private String description;
    private JSONObject posterURLs;
    private JSONObject fullInfo;
    private HashMap<String, Review> myReviews;

    /**
     * Creates a placeholder movie for when Internet connection is unavailable
     *
     * @param j used in MovieListFragment to mark spot of placeholder Movie
     */
    public Movie(int j) {
        if (j == -1) {
            this.init();
        } else {
            title = "Title" + j;
            rating = 10;
            description = "Description of the movie number " + j
                    + " of the list of movies";
            myReviews = new HashMap<String, Review>();
        }
    }

    /**
     * Accepts a JSONObject and creates a Movie out of it
     *
     * @param jObj the JSONObject to base this Movie off of
     */
    public Movie(JSONObject jObj) {
        fullInfo = jObj;
        JSONArray tmpJArray;
        myReviews = new HashMap<String, Review>();
        try {
            id = fullInfo.getInt("id");
            title = fullInfo.getString("title");
            description = fullInfo.getString("synopsis");
            rating = fullInfo.getJSONObject("ratings").getInt("critics_score");
            posterURLs = fullInfo.getJSONObject("posters");
            posterURL = posterURLs.getString("thumbnail");
//            tmpJArray = fullInfo.getJSONArray("genres");
//            for (int i = 0; i < tmpJArray.length(); i++) {
//                genres.add(Genre.toGenre(tmpJArray.getString(i)));
//            }
        } catch (JSONException e) {
            Log.e("JSON Error", "JSONException while parsing single movie" + e.toString());
        }
    }

    /**
     * Add a review to this movie's hashmap
     * @param username the username of the user who rated this movie
     * @param score the score the user gave the movie
     * @param comment the user's comment
     */
    public void addReview(String username, int score, String comment) {
        if(myReviews.containsKey(username)) {
            throw new IllegalArgumentException(username + " has already reviewed movieID " +
                    id);
        } else {
            myReviews.put(username, new Review(score, comment));
        }
    }

    /**
     * Remove a review from this movie's hashmap
     * @param username the username's review to remove
     */
    public void removeReview(String username) {
        if(!myReviews.containsKey(username)) {
            throw new IllegalArgumentException(username + " has NOT reviewed movieID " +
                    id);
        } else {
            myReviews.remove(username);
        }
    }

    /**
     * Get a movie review from this movie's hashmap
     * @param username the username in hashmap of the review to get
     * @return the review corresponding to this movieID
     */
    public Review getReview(String username) {
        if(!myReviews.containsKey(username)) {
            throw new IllegalArgumentException(username + " has NOT reviewed movieID " +
                    id);
        } else {
            return myReviews.get(username);
        }
    }

    @Override
    public int compareTo(Movie other) {
        return this.id - other.getID();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Movie)) {
            return false;
        }
        return this.compareTo((Movie) obj) == 0;
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
     * Returns the rating of the Movie
     *
     * @return the rating of the Movie
     */
    public int getRating() {
        return rating;
    }

    /**
     * This is a method hidden from Matt.
     * If you find this don't tell Matt.
     */
    private void init() {
        id = 37737;
        title = "Matt the Great and Supreme";
        description = "Matt is a Great Leader of the Team 19 (18). Known for his " +
                "battle prowess and unmatched sex appeal, those who knew him " +
                "mentioned him as the LORD";
        rating = 101;
    }
    /**
     * Returns the description of the Movie
     *
     * @return the description of the Movie
     */
    public String getDescription() {
        return description;
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
     * Returns the String representing the URL of the movie poster
     *
     * @return the String representing the URL of the movie poster
     */
    public String getPosterURL() {
        return posterURL;
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