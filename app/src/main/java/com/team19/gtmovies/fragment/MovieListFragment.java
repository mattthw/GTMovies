package com.team19.gtmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.team19.gtmovies.R;
import com.team19.gtmovies.SingletonMagic;
import com.team19.gtmovies.activity.MovieDetailActivity;
import com.team19.gtmovies.activity.MovieListActivity;
import com.team19.gtmovies.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieListFragment extends Fragment {
    private static List<List<Movie>> tabMovieList = null;
    private static int currentTab = 0;
    private static List<Movie> searchMovieList = null;
    private static boolean mTwoPane = false;
    private boolean search = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    /**
     * creates new MovieListFragment instance
     * @param page number for page
     * @return new MovieListFragment instance
     */
    public static MovieListFragment newInstance(int page) {
        Log.d("GTMovie", "create new fragment");
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARG_ITEM_ID, page + 1);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(mBundle);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list, container, false);

        Log.d("GTMovie", "fragment onCreateView");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_view);
        if (rootView.findViewById(R.id.movie_list_view) == null) {
            Log.e("GTMovies", "Well there's your problem");
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        if (tabMovieList == null) {
            fillTabMovieList(null);
        }

        if (search) {
            if (searchMovieList != null) {
                mAdapter = new MovieRecyclerViewAdapter(searchMovieList);
            } else {
                mAdapter = new MovieRecyclerViewAdapter(tabMovieList.get(currentTab));
            }
            search = false;
        } else {
            Log.e("GTMovies: onCreateView", currentTab + "");
            mAdapter = new MovieRecyclerViewAdapter(tabMovieList.get(currentTab));
        }

        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Log.e("GTMovies", "This shouldn't be null :)");
        }

        if (rootView.findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;                                                //TODO: implement twopane check
        }

        return rootView;
    }

    /**
     * Fills movie list if able to. If unable to, sets movieList to default.
     * @param list list to set movie list to
     * @return true if successfully set to provided argument false if unable to
     */
    public static boolean fillTabMovieList(List<List<Movie>> list) {
        if (list != null ) {
            tabMovieList = list;
            return true;
        } else {
            tabMovieList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ArrayList<Movie> arrayList = new ArrayList<>();
                for (int j = 0; j < 3 * (i + 1); j++) {
                    Movie movie;
                    movie = new Movie(j);
                    arrayList.add(movie);
                }
                tabMovieList.add(arrayList);
            }
        }
        return true;
    }

    /**
     * Fills movie list on search
     * @param list new list of movies from search query
     * @return boolean if list can be used to replace movie search list
     */
    public static boolean fillSearchMovieList(List<Movie> list) {
        if (list != null) {
            Log.d("GTMovies", list.toString());
            searchMovieList = list;
            return true;
        } else {
            searchMovieList = new ArrayList<>();
            return false;
        }
    }

    /**
     * Sets current tab
     * @param position position of current tab
     * @return true if successfully set
     */
    public static void setTabPosition(int position) {
        currentTab = position % 3;
        Log.e("GTMovies: setTab", position + " : " + (position % 3));
    }


    /**
     * Sets fragment to display search
     */
    public void setSearch() {
        search = true;
    }

    /**
     * A getter for where or not able to display in two panes
     * @return true if able to display in two panes
     */
    public static boolean isTwoPane() {
        return mTwoPane;
    }

    /**
     * Innter class for recyclerview adapter
     */
    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {
        private List<Movie> movieList;

        /**
         * inner class for RecyclerView.ViewHolder
         */
        public class MovieViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final NetworkImageView mMoviePosterView;
            public final TextView mMovieTitleView;
            public final TextView mMovieRatingView;
            public final TextView mMovieDescriptionView;
            public Movie mMovieInfo;

            /**
             * public constructor for viewholder
             * @param itemView current view
             */
            public MovieViewHolder(View itemView) {
                super(itemView);

                Log.d("GTMovie", "create new MovieViewHolder");

                mView = itemView;
                mMoviePosterView = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
                mMoviePosterView.setDefaultImageResId(R.mipmap.slowpoke);
                mMoviePosterView.setErrorImageResId(R.mipmap.load_error3);
                mMovieTitleView = (TextView) itemView.findViewById(R.id.movie_title);
                mMovieRatingView = (TextView) itemView.findViewById(R.id.movie_rating);
                mMovieDescriptionView = (TextView) itemView.findViewById(R.id.movie_description);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMovieTitleView.getText() + "'";
            }
        }

        /**
         * Public constructor for adapter
         * @param movieInfo list of movies
         */
        public MovieRecyclerViewAdapter(List<Movie> movieInfo) {
            movieList = movieInfo;

            Log.d("GTMovie", "create new MovieRecyclerViewAdapter");
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.movie_list_content, parent, false);

            Log.d("GTMovie", "onCreateViewHolder");
            return new MovieViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MovieRecyclerViewAdapter.MovieViewHolder holder, int position) {
            holder.mMovieInfo = movieList.get(position);

            Log.d("GTMovie", "pnBindViewHolder");
            //holder.poster.setImageResource(mMovieInfo.poster);
            if (holder != null) {
                Log.d("GTMovie", "holder not null");
                holder.mMoviePosterView.setImageUrl(
                        holder.mMovieInfo.getPosterURL(),
                        SingletonMagic.getInstance(getContext()).getImageLoader());
                holder.mMovieTitleView.setText(holder.mMovieInfo.getTitle());
                String rating = holder.mMovieInfo.getRating() + "%";
                holder.mMovieRatingView.setText(rating);
                holder.mMovieDescriptionView.setText(holder.mMovieInfo.getDescription());
            } else {
                Log.e("GTMovies", "No holder.");
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("GTMovie", "holder setOnClickListener");
                    if (MovieListFragment.isTwoPane()) {
                        Log.d("GTMovie", "TWO PANE PROBLEM!!!");
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mMovieInfo.getTitle());
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        if (!fragment.isVisible()) {
                            Log.d("GTMovie", "one pane");
                            Context context = v.getContext();
                            Intent intent = new Intent(context, MovieDetailActivity.class);
                            intent.putExtra(MovieDetailFragment.ARG_ITEM_ID,
                                    holder.mMovieInfo.getTitle());
                            intent.putExtra(MovieDetailFragment.ARG_ITEM_DESC,
                                    holder.mMovieInfo.getDescription());
                            intent.putExtra(MovieDetailFragment.ARG_ITEM_RATE,
                                    holder.mMovieInfo.getRating() + "%");

                            context.startActivity(intent);
                        }
                        /*getFragmentManager().beginTransaction()
                                .replace(R.id.content_main, fragment)
                                .commit();*/
                    } else {
                        Log.d("GTMovie", "one pane");
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID,
                                holder.mMovieInfo.getTitle());
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_DESC,
                                holder.mMovieInfo.getDescription());
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_RATE,
                                holder.mMovieInfo.getRating());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

    /**
     * getter for search movie list
     * 
     * @return List of movies in current search movie list
     */
    public static List getSearchMovieList() {
        return searchMovieList;
    }
}
