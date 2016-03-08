package com.team19.gtmovies.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.fragment.MovieDetailFragment;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 * @author Austin Leal
 * @version 1.0
 */
public class MovieDetailActivity extends AppCompatActivity {

    private FragmentManager fm;
    private ReviewDialogFragment reviewDialog;
    private static int score = 0;
    private static String comment = "null";
    private static Intent gotIntent;
    private static View rootView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gotIntent = getIntent();
        rootView = findViewById(R.id.movie_detail_container);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            //add info about movie from tomato API to fragment args
            arguments.putString(MovieDetailFragment.ARG_ITEM_TITLE,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_TITLE));
            arguments.putInt(MovieDetailFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(MovieDetailFragment.ARG_ITEM_ID, -1));
            arguments.putString(MovieDetailFragment.ARG_ITEM_DESC,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_DESC));
            arguments.putString(MovieDetailFragment.ARG_ITEM_RATE,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_RATE));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        fm = getSupportFragmentManager();
        reviewDialog = new ReviewDialogFragment();
        //open review dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDialog.show(fm, "fragment_edit_name");
            }
        });
    }

    /**
     * inner anonymous class to create review dialog fragment
     */
    /**
     * Class needed for spinner XML item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            navigateUpTo(new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * inner class for dialog
     */
    public static class ReviewDialogFragment extends DialogFragment {
        protected Spinner scoreSpin;
        private Integer tempScore;
        private EditText mComment;

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //make builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            //setup Spinner for user rating
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_review, null);
            scoreSpin = (Spinner)view.findViewById(R.id.spinnerScore);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.scores, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            scoreSpin.setAdapter(adapter);
            addListenerOnSpinnerItemSelection();

            mComment = (EditText) view.findViewById(R.id.commentView);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            comment = mComment.getText().toString();
                            score = tempScore;
                            Log.println(Log.INFO, "GTMovies", "SCORE: " + score);
                            Log.println(Log.INFO, "GTMovies", "COMMENT: " + comment);
                            //get movie id
                            Integer tempID = gotIntent.getIntExtra(MovieDetailFragment.ARG_ITEM_ID, -1);
                            //save the review
                            try {
                                IOActions.SaveNewRating(tempID,score,comment);
                            } catch (IllegalArgumentException e) {
                                Log.println(Log.ERROR, "GTMovies", e.getMessage());
                                Snackbar.make(rootView, "Movie already reviewed.", Snackbar.LENGTH_SHORT).show();
                            }
                            Log.println(Log.DEBUG, "GTMovies", "Movies: " + IOActions.getMovies());
                            MovieDetailFragment frag = (MovieDetailFragment) getFragmentManager()
                                    .findFragmentById(R.id.movie_detail_container);
                            frag.updateFrag();

                        }
                    })
                    //close dialog
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ReviewDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        /**
         * add listener to spinner item
         */
        public void addListenerOnSpinnerItemSelection() {
            scoreSpin.setOnItemSelectedListener(new ScoreSpinnerListener());
        }
        private class ScoreSpinnerListener implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                try {
                    tempScore = Integer.parseInt((String)parent.getItemAtPosition(pos));
                } catch (Exception e) {
                    Log.println(Log.ERROR, "GTMovies", "Couldint parse selected score as Integer");
                    tempScore = 0;
                }
                Log.println(Log.INFO, "GTMovies", "selected: " + tempScore.toString());
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        }
    }


}
