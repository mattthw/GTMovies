package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.actions.SearchIntents;
import com.team19.gtmovies.R;
import com.team19.gtmovies.data.SingletonMagic;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to be searched
 */
public class SearchActivity extends AppCompatActivity {
    private List<Movie> list = new ArrayList<>();
    private boolean nextable = true;
    private boolean prevable = false;
    private String nextURL = null;
    private String prevURL = null;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.d("GTMovies", "Got to SEARCH");
        fragmentManager = getSupportFragmentManager();

        handleIntent(getIntent());
//        Intent intent = getIntent();
//        if (SearchIntents.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            // do the Search
//        } else {
//            handleIntent(getIntent());
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("GTMovies", "Got to onNewIntent");
        handleIntent(intent);
    }

    /**
     * Handles intents in order to fetch query and display results.
     *
     * @param intent intent to manage
     */
    private void handleIntent(Intent intent) {
        Log.d("GTMovies", "Got to handleIntent");
        String query = "";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d("Search", "Old Route");
            query = intent.getStringExtra(SearchManager.QUERY);
        } else if (SearchIntents.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d("VoiceSearch", "VoiceSearch is actually different");
            query = intent.getStringExtra(SearchManager.QUERY);
        } else {
            // something is wrong just return
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Log.d("GTMovies", "Hitting actionBar in handleIntent with query:- " +  query);
            actionBar.setTitle(query);
        }
        String queryURI = Uri.encode(query);
        if (query.equals("Matt McCoy")) {
            list.add(new Movie(-1));
            Toast.makeText(
                    getApplicationContext(),
                    "Achievement Unlocked!!!",
                    Toast.LENGTH_LONG).show();
        }
        // Creating the JSONRequest
        String urlRaw = String.format(
                SingletonMagic.BASE_URL, SingletonMagic.SEARCH, "q=" + queryURI, SingletonMagic.PROF_KEY);

        JsonObjectRequest searchRequest = new JsonObjectRequest
                (Request.Method.GET, urlRaw, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                        if (resp == null) {
                            Log.e("JSONRequest ERROR", "Null Response Received");
                        }

                        // put movies into a JSONArray
                        JSONArray tmpMovies = null;
                        try {
                            tmpMovies = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies in Search.");
                        }
                        if (tmpMovies == null) {
                            Log.e("Movie Error", "movies JSONArray is null!");
                        }
                        Log.e("WHEE", Integer.toString(tmpMovies.length()));
                        for (int i = 0; i < tmpMovies.length(); i++) {
                            try {
                                list.add(new Movie(tmpMovies.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.e("Movie Error", "Couldn't make Movie" + i + "in Search");
                            }
                        }
                        try {
                            JSONObject tmpJ = resp.getJSONObject("links");
                            nextURL = tmpJ.getString("next");
                            prevURL = tmpJ.getString("prev");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Fail to get connected URLs in SEARCH");
                        }
                        nextable = nextURL != null;
                        prevable = prevURL != null;

                        // AUSTIN THING JUST CTRL C V-ed
                        Log.e("GTMovies", "TabsSearch");
                        MovieListFragment.setSearchMovieList(list);
                        MovieListFragment movieListFragment = MovieListFragment.newInstance(
                                MovieListFragment.SEARCH);
                        Log.d("GTMovies", "line1");
                        fragmentManager.beginTransaction().replace(R.id.search_frame_layout,
                                movieListFragment).commit();
                        Log.d("GTMovies", "line2");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });
        SingletonMagic.getInstance(this).addToRequestQueue(searchRequest);
    }

    /*public void setupSearch() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.search_search_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("GTMovies", "break here");
                //searchView.setQuery(query, true);
                onNewIntent(getSupportParentActivityIntent());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }*/

    //////////////////////////////////
    // DIDNT KNOW HOW THIS WOULD BE //
    // USED IN ACTUAL APP SO I JUST //
    // HAVE THEM WRITTEN DOWN FOR   //
    // REFERENCE PURPOSES.          //
    //                   JINU JANG  //
    //////////////////////////////////

    /**
     * Grabs the JSONObject from the next Page
     *
     * @throws UnsupportedOperationException when no nextPage exists currently
     */
    public void grabNextPage() throws UnsupportedOperationException {
        if (!nextable) {
            throw new UnsupportedOperationException("No next page found");
        }
        list = new ArrayList<Movie>();
        JsonObjectRequest newMovieRequest = new JsonObjectRequest
                (Request.Method.GET, nextURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                        if (resp == null) {
                            Log.e("JSONRequest ERROR", "Null Response Received");
                        }

                        // put movies into a JSONArray
                        JSONArray tmpMovies = null;
                        try {
                            tmpMovies = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies in Search.");
                        }
                        if (tmpMovies == null) {
                            Log.e("Movie Error", "movies JSONArray is null!");
                        }
                        for (int i = 0; i < tmpMovies.length(); i++) {
                            try {
                                list.add(new Movie(tmpMovies.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.e("Movie Error", "Couldn't make Movie" + i + "in Search");
                            }
                        }
                        try {
                            JSONObject tmpJ = resp.getJSONObject("links");
                            nextURL = tmpJ.getString("next");
                            prevURL = tmpJ.getString("prev");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Fail to get connected URLs in SEARCH");
                        }
                        nextable = nextURL != null;
                        prevable = prevURL != null;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });

        // Access the RequestQueue through singleton class.
        // Add Requests to RequestQueue
        SingletonMagic.getInstance(this).addToRequestQueue(newMovieRequest);
    }

    /**
     * Grabs the JSONObject from the previous Page
     *
     * @throws UnsupportedOperationException when no prevPage exists currently
     */
    public void grabPrevPage() throws UnsupportedOperationException {
        if (!prevable) {
            throw new UnsupportedOperationException("No previous page found");
        }
        list = new ArrayList<Movie>();
        JsonObjectRequest newMovieRequest = new JsonObjectRequest
                (Request.Method.GET, prevURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                        if (resp == null) {
                            Log.e("JSONRequest ERROR", "Null Response Received");
                        }

                        // put movies into a JSONArray
                        JSONArray tmpMovies = null;
                        try {
                            tmpMovies = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Error when getting movies in Search.");
                        }
                        if (tmpMovies == null) {
                            Log.e("Movie Error", "movies JSONArray is null!");
                        }
                        for (int i = 0; i < tmpMovies.length(); i++) {
                            try {
                                list.add(new Movie(tmpMovies.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.e("Movie Error", "Couldn't make Movie" + i + "in Search");
                            }
                        }
                        try {
                            JSONObject tmpJ = resp.getJSONObject("links");
                            nextURL = tmpJ.getString("next");
                            prevURL = tmpJ.getString("prev");
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", "Fail to get connected URLs in SEARCH");
                        }
                        nextable = nextURL != null;
                        prevable = prevURL != null;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY FAIL", "Couldn't getJSON");
                    }
                });

        // Access the RequestQueue through singleton class.
        // Add Requests to RequestQueue
        SingletonMagic.getInstance(this).addToRequestQueue(newMovieRequest);
    }

}
