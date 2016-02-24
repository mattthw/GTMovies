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
import android.widget.ImageView;
import android.widget.TextView;

import com.team19.gtmovies.R;
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
    private List<Movie> searchMovieList = null;

    private static boolean mTwoPane = false;

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_list_view);
        if (rootView.findViewById(R.id.movie_list_view) == null) {
            Log.e("GTMovies", "Well there's your problem");
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        if (tabMovieList == null) {
            fillTabMovieList(null);
        }

        if (searchMovieList != null) {
            mAdapter = new MovieRecyclerViewAdapter(searchMovieList);
        } else {
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
            mTwoPane = true;
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

    public boolean fillSearchMovieList(List<Movie> list) {
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
    public static boolean setTabPosition(int position) {
        if (position >= 0 && position < tabMovieList.size()) {
            currentTab = position;
            return true;
        }
        return false;
    }

    /**
     * A getter for where or not able to display in two panes
     * @return true if able to display in two panes
     */
    public static boolean isTwoPane() {
        return mTwoPane;
    }

    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {
        private List<Movie> movieList;

        /**
         * inner class for RecyclerView.ViewHolder
         */
        public class MovieViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mMoviePosterView;
            public final TextView mMovieTitleView;
            public final TextView mMovieRatingView;
            public final TextView mMovieDescriptionView;
            public Movie mMovieInfo;

            public MovieViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mMoviePosterView = (ImageView) itemView.findViewById(R.id.movie_poster);
                mMovieTitleView = (TextView) itemView.findViewById(R.id.movie_title);
                mMovieRatingView = (TextView) itemView.findViewById(R.id.movie_rating);
                mMovieDescriptionView = (TextView) itemView.findViewById(R.id.movie_description);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMovieTitleView.getText() + "'";
            }
        }

        public MovieRecyclerViewAdapter(List<Movie> movieInfo) {
            movieList = movieInfo;
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.movie_list_content, parent, false);
            return new MovieViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MovieRecyclerViewAdapter.MovieViewHolder holder, int position) {
            holder.mMovieInfo = movieList.get(position);
            //holder.poster.setImageResource(mMovieInfo.poster);
            if (holder != null) {
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
                    if (MovieListFragment.isTwoPane()) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mMovieInfo.getTitle());
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content_main, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mMovieInfo.getTitle());

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

    public static List getMovieList() {
        return tabMovieList.get(0);
    }
}
