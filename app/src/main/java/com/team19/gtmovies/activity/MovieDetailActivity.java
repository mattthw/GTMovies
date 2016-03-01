package com.team19.gtmovies.activity;

import android.app.Dialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.team19.gtmovies.R;
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

    protected Spinner scoreSpin;
    protected int score = 0;
    protected String comment = "";
    private FragmentManager fm;
    private ReviewDialogFragment reviewDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
            arguments.putString(MovieDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_ID));
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

        //spinner adapter setup
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_review, null);
        scoreSpin = (Spinner)dialogView.findViewById(R.id.spinnerScore);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.profMajors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSpin.setAdapter(adapter);
        //scoreSpin.setSelection(2);
        addListenerOnSpinnerItemSelection();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewDialog();
            }
        });
    }

    public void showReviewDialog() {
        reviewDialog.show(fm, "fragment_edit_name");
    }
    /**
     * inner anonymous class to create review dialog fragment
     */
    public static class ReviewDialogFragment extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_review, null))
                    // Add action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ReviewDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * add listener to spinner item
     */
    public void addListenerOnSpinnerItemSelection() {
        scoreSpin.setOnItemSelectedListener(new ScoreSpinnerListener());
    }

    /**
     * Class needed for spinner XML item
     */
    private class ScoreSpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
//            Toast.makeText(parent.getContext(), "Selected Country : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
            score = (Integer) parent.getItemAtPosition(pos);
//            Log.println(Log.INFO, "GTMovies", "selected: " + parent.getItemAtPosition(pos).toString());
        }
        @Override
        public void onNothingSelected(AdapterView parent) {
        }
    }
}
