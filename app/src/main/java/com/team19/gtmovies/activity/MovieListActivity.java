package com.team19.gtmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.fragment.MovieDetailFragment;
import com.team19.gtmovies.pojo.Movie;

import java.util.List;

/**
 * An activity representing a list of Movie. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private int mPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_list);
        Log.i("GTMovies", "going to activity");

        View recyclerView = findViewById(R.id.movie_list_view);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            //TODO: check if twoPane
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            //mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the recyclerview
     * @param recyclerView to setup
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }


    /**
     * Inner class to extend RecyclerViewAdapter
     */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> mValues;

        /**
         * Public SimpleItemRecyclerViewAdapter constructer
         * @param items List of items for adapter
         */
        public SimpleItemRecyclerViewAdapter(List<Movie> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 10000; j++) {
                    Log.i("Useless", "This one might actually be it.");
                }
                for (int j = 0; j < 10000; j++) {
                    Log.i("Useless", "But probably isn't");
                }
            }
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
//            //holder.mMoviePosterView.setImageResource(mValues.get(position).);
//            holder.mMovieTitleView.setText(mValues.get(position).id);
//            holder.mMovieRatingView.setText(mValues.get(position).content);
//            holder.mMovieDescriptionView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        //arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();*/
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        //intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * Inner ViewHolder class
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mMoviePosterView;
            public final TextView mMovieTitleView;
            public final TextView mMovieRatingView;
            public final TextView mMovieDescriptionView;
            public Movie mItem;

            /**
             * Public constructor for ViewHolder
             * @param view view to be held
             */
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mMoviePosterView = (ImageView) view.findViewById(R.id.movie_poster);
                mMovieTitleView = (TextView) view.findViewById(R.id.movie_title);
                mMovieRatingView = (TextView) view.findViewById(R.id.movie_rating);
                mMovieDescriptionView = (TextView) view.findViewById(R.id.movie_description);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMovieTitleView.getText() + "'";
            }
        }
    }
}
