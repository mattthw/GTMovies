package com.team19.gtmovies.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team19.gtmovies.R;
import com.team19.gtmovies.SingletonMagic;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    List<Movie> list = new ArrayList<>();
    public boolean nextable = true;
    public boolean prevable = false;
    private String nextURL = null;
    private String prevURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());

        Log.d("GTMovies", "Got to search");

        // Setup action bar
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.d("GTMovies", "Got past actionBar in onCreate");*/

        // Layout toolbar and search
        //Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        //setSupportActionBar(toolbar);
        //windowActionBar
        //setupSearch();

//        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("GTMovies", "Got to onNewIntent");
        handleIntent(intent);
    }

    /**
     * Handles intents in order to fetch query and display results.
     * @param intent intent to manage
     */
    private void handleIntent(Intent intent) {
        Log.d("GTMovies", "Got to handleIntent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d("GTMovies", "if true");
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("GTMovies", "query worked");
            //Set page title to query
            ActionBar actionBar = getSupportActionBar();
            Log.d("GTMovies", "got actionBar");
            if (actionBar != null) {
                Log.d("GTMovies", "Hitting actionBar in handleIntent with query:- " +  query);
                actionBar.setTitle(query);
            }

            String queryURI = Uri.encode(query);
            // Creating the JSONRequest
            String urlRaw = String.format(
                    SingletonMagic.baseURL, SingletonMagic.search, "q=" + queryURI, SingletonMagic.profKey);

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
                                Log.e("JSON ERROR", "Fail to get connected URLs in search");
                            }
                            if (nextURL != null) {
                                nextable = true;
                            } else {
                                nextable = false;
                            }
                            if (prevURL != null) {
                                prevable = true;
                            } else {
                                prevable = false;
                            }

                            // AUSTIN THING JUST CTRL C Ved
                            MovieListFragment movieListFragment = MovieListFragment.newInstance(0);
                            movieListFragment.fillSearchMovieList(list);
                            Log.d("GTMovies", "line1");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            if (fragmentManager != null) {
                                fragmentManager.beginTransaction().replace(R.id.search_frame_layout,
                                        movieListFragment).commit();
                                Log.d("GTMovies", "line2");
                            } else {
                                Log.e("GTMovies", "No fragmentmanager");
                            }
                            Log.d("GTMovies", "here1");
                            //  movieListFragment.fillSearchMovieList(null);    //nulls for next query
                            Log.d("GTMovies", "here2");

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY FAIL", "Couldn't getJSON");
                        }
                    });

            // Access the RequestQueue through singleton class.
            // Add Requests to RequestQueue
            SingletonMagic.getInstance(this).addToRequestQueue(searchRequest);
        }
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
     * @return the JSONObject from the next Page
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
                            Log.e("JSON ERROR", "Fail to get connected URLs in search");
                        }
                        if (nextURL != null) {
                            nextable = true;
                        } else {
                            nextable = false;
                        }
                        if (prevURL != null) {
                            prevable = true;
                        } else {
                            prevable = false;
                        }
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
     * @return the JSONObject from the previous Page
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
                            Log.e("JSON ERROR", "Fail to get connected URLs in search");
                        }
                        if (nextURL != null) {
                            nextable = true;
                        } else {
                            nextable = false;
                        }
                        if (prevURL != null) {
                            prevable = true;
                        } else {
                            prevable = false;
                        }
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
