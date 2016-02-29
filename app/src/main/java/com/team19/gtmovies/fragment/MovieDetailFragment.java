package com.team19.gtmovies.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.activity.MovieDetailActivity;
import com.team19.gtmovies.activity.MovieListActivity;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Movie;

import org.w3c.dom.Text;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_DESC = "item_description";
    public static final String ARG_ITEM_RATE = "item_rating";

    /**
     * The dummy content this fragment is presenting.
     */
    private Movie mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    /**
     * creates new MovieDetailFragment instance
     * @return new MovieDetailFragment instance
     */
    public static MovieDetailFragment newInstance() {
        return new MovieDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = MovieListFragment.getSearchMovieList().getValue(getArguments().getString(ARG_ITEM_ID)); //TODO: @Jinu using string title of movie to get movie to display in this view
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_ITEM_ID));
//                Log.println(Log.INFO, "GTMovies", "ARG_ITEM_ID:" + getArguments().getString(ARG_ITEM_ID));
//                mItem = IOActions.getMovieByTitle(getArguments().getString(ARG_ITEM_ID));
//                if (mItem == null) {
//                    Log.println(Log.ERROR, "GTMovies", "mItem in MovieDetailFragment is null!");
//                    Log.println(Log.ERROR, "GTMovies", IOActions.getMovies().toString());
//                } else {
//                    Log.println(Log.INFO, "GTMovies", mItem.toString());
//                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.movie_detail)).setText(mItem.getTitle());
            getActivity().setTitle(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.ratingView)).setText(mItem.getRating());

        }
        if (getArguments().containsKey(ARG_ITEM_DESC)) {
            ((TextView) rootView.findViewById(R.id.detailView))
                    .setText(getArguments().getString(ARG_ITEM_DESC));
        } else {
            Log.println(Log.ERROR, "GTMovie", "No description for movie");
        }
        if (getArguments().containsKey(ARG_ITEM_RATE)) {
            ((TextView) rootView.findViewById(R.id.ratingView))
                    .setText(getArguments().getString(ARG_ITEM_RATE));
        } else {
            Log.println(Log.ERROR, "GTMovie", "No rating for movie");
        }
        return rootView;
    }
}
