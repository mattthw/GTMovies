package com.team19.gtmovies.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.activity.MovieDetailActivity;
import com.team19.gtmovies.activity.MovieListActivity;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;

import java.util.ArrayList;
import java.util.Arrays;

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
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_DESC = "item_description";
    public static final String ARG_ITEM_RATE = "item_rating";

    private TextView userRatingView;
//    private static ListView commentsList;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private Intent userIntent = null;
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

        Log.e("MovieDetailFragment", "onCreate " + Arrays.toString(Thread.currentThread().getStackTrace()));

        if (getArguments().containsKey(ARG_ITEM_TITLE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_ITEM_TITLE));
            }
        }
        mItem = IOActions.getMovieById(getArguments().getInt(ARG_ITEM_ID, -1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        //set description
        if (getArguments().containsKey(ARG_ITEM_DESC)) {
            ((TextView) rootView.findViewById(R.id.detailView))
                    .setText(getArguments().getString(ARG_ITEM_DESC));
        } else {
            Log.println(Log.ERROR, "GTMovie", "No description for movie");
        }
        //set tomato rating
        if (getArguments().containsKey(ARG_ITEM_RATE)) {
            if (!getArguments().getString(ARG_ITEM_RATE).contains("-1")) {
                ((TextView) rootView.findViewById(R.id.ratingView))
                        .setText(getArguments().getString(ARG_ITEM_RATE));
            }
        } else {
            Log.println(Log.ERROR, "GTMovie", "No rating for movie");
        }
        userRatingView = ((TextView) rootView.findViewById(R.id.userRatingView));
        mainListView = (ListView) rootView.findViewById(R.id.commentListView);
        //if users have review this movie then it will exist
        //and we will get their averaged scores.
        if (mItem != null) {
            int temp = mItem.getUserRating();
            //set user rating
            userRatingView.setText(temp + "%");
            Log.println(Log.DEBUG, "GTMovies", "user rating: " + temp + "");
        } else {
            Log.println(Log.DEBUG, "GTMovies", "mItem is null in MovieDetailFragment");
        }
        populateList();
        return rootView;
    }
    private void populateList() {
        // Find the ListView resource.
//        mainListView = (ListView) getActivity().findViewById( R.id.commentListView );
        if (mItem == null) {
            Log.i("GTMovies", "Didn't populate review list because local movie object null for "
                    + getArguments().getString(ARG_ITEM_TITLE));
            return;
        }
//        Object[] reviewList = mItem.getReviews().values().toArray();
        Object[] reviewList = IOActions.getListMovieReviews(getArguments().getInt(ARG_ITEM_ID, -1)).toArray();
        ArrayList<String> commentList = new ArrayList<>(reviewList.length);
        double total = 0;
        int userCount = 0;
        for (Object o : reviewList) {
            //average user score
            total += ((Review)o).getScore();
            userCount++;
            //review coments
            String s = ((Review)o).getComment();
            String u = ((Review)o).getUsername();
            int mID = ((Review)o).getMovieID();
            if (s.length() > 0
                    && mID == getArguments().getInt(ARG_ITEM_ID, -1)) {
                commentList.add(u + ": " + s);
            }
        }

        if(userCount == 0) { // in case of divide by zero
            userRatingView.setText("N/A");
        } else {
            final int REVIEWMULTIPLIER = 20;
            int tempScore = ((int)((total/((double)userCount)) * REVIEWMULTIPLIER));
            userRatingView.setText(tempScore + "");
        }
        Log.println(Log.DEBUG, "GTMovies", commentList.toString());
        // Create ArrayAdapter using the comments list.
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, commentList);
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

        setListViewHeightBasedOnChildren(mainListView);
    }

    /**
     * public code used to update listView height after dynamically adding items to it.
     * source: http://stackoverflow.com/questions/29512281
     * /how-to-make-listviews-height-to-grow-after-adding-items-to-it
     * @param listView list in question
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        Log.e("Listview Size ", "" + listView.getCount());
        ListAdapter listAdapter1 = listView.getAdapter();
        if (listAdapter1 == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter1.getCount(); i++) {
            View listItem = listAdapter1.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter1.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
    public boolean updateFrag() {
        //update user rating
        mItem = IOActions.getMovieById(getArguments().getInt(ARG_ITEM_ID, -1));
        userRatingView.setText(mItem.getUserRating() + "%");
        populateList();
        return true;
    }
}
