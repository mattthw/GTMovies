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
    //private static List<List<Movie>> tabMovieList = null;
    public static final int NEW_MOVIES_TAB = 0;
    public static final int TOP_RENTALS_TAB = 1;
    public static final int YOUR_RECOMMENDATIONS_TAB = 2;
    public static final int SEARCH = 3;

    private static int currentTab = 0;

    private static List<Movie> newMoviesList = null;
    private static List<Movie> topRentalsList = null;
    private static List<Movie> yourRecommendationsList = null;
    private static List<Movie> searchMovieList = null;

    private static boolean mTwoPane = false;
    private static boolean tabs = false;
    private boolean search = false;

    /*private static final MovieListFragment tab0 = new MovieListFragment(0);
    private static final MovieListFragment tab1 = new MovieListFragment(1);
    private static final MovieListFragment tab2 = new MovieListFragment(2);
    private static final MovieListFragment searchFragment = new MovieListFragment(3);*/

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /*
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     * @param page number for page
     */
    /*private MovieListFragment(int page) { //TODO: The comment above indicates this change will be a problem later
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARG_ITEM_ID, page);
        setArguments(mBundle);
    }*/

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    /*
     * creates new MovieListFragment instance
     * @param page number for page
     * @return new MovieListFragment instance
     */
    public static MovieListFragment newInstance(int page) {
        /*switch (page) { //this was commented out before
            case 0: return tab0;
            case 1: return tab1;
            case 2: return tab2;
            case 3: return searchFragment;
            default: return null;
        }*/
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARG_ITEM_ID, page);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(mBundle);
        return fragment;
    }

    /*
     * obtains MovieListFragment instance
     * @param page number for page
     * @return current MovieListFragment instance
     */
    /*public static MovieListFragment getInstance(int page) {
        switch (page) {
            case 0: return tab0;
            case 1: return tab1;
            case 2: return tab2;
            case 3: return searchFragment;
            default: return null;
        }
    }*/

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

        /*
        if (search) {
            if (searchMovieList != null) {
                mAdapter = new MovieRecyclerViewAdapter(searchMovieList);
            }
            search = false;
        } else if (getArguments() != null) {
            Log.d("GTMovies: onCreateView", currentTab + "");
            mAdapter = new MovieRecyclerViewAdapter(tabMovieList.get(getArguments().getInt(ARG_ITEM_ID)));
            //Log.d("GTMovies", tabMovieList.get(currentTab).toString());
            tabs = false;
        } else if (getArguments() != null) {
            Log.d("GTMovies: getargs", getArguments().toString());
        } else {
            Log.e("GTMovies:", "No bundle.");
        }*/
        if (getArguments() != null) {
            switch (getArguments().getInt(ARG_ITEM_ID)) {
                case NEW_MOVIES_TAB:
                    mAdapter = new MovieRecyclerViewAdapter(newMoviesList);
                    break;
                case TOP_RENTALS_TAB:
                    mAdapter = new MovieRecyclerViewAdapter(topRentalsList);
                    break;
                case YOUR_RECOMMENDATIONS_TAB:
                    mAdapter = new MovieRecyclerViewAdapter(yourRecommendationsList);
                    break;
                case SEARCH:
                    mAdapter = new MovieRecyclerViewAdapter(searchMovieList);
                    break;
                default:
                    Log.e("GTMovies", "Incorrect int for fragment list.");
            }
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
            mTwoPane = true;                        //TODO: implement twopane check for extra credit
        }

        return rootView;
    }

    /*
     * Fills movie list if able to. If unable to, sets movieList to default.
     * @param list list to set movie list to
     * @return true if successfully set to provided argument false if unable to
     */
    /*public static boolean fillTabMovieList(List<List<Movie>> list) {
        if (list != null) {
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
        return false;
    }*/

    /**
     * Changes newMovieList.
     * @param list list to set new movies list to
     * @return true if successfully set to provided argument false if unable to
     */
    public static boolean setNewMoviesList(List<Movie> list) {
        if (list != null) {
            newMoviesList = list;
            return true;
        }
        return false;
    }

    /**
     * Changes topRentalsList.
     * @param list list to set top rentals list to
     * @return true if successfully set to provided argument false if unable to
     */
    public static boolean setTopRentalsList(List<Movie> list) {
        if (list != null) {
            topRentalsList = list;
            return true;
        }
        return false;
    }

    /**
     * Changes movielist.
     * @param list list to set recommendations to
     * @return true if successfully set to provided argument false if unable to
     */
    public static boolean setYourRecommendationsList(List<Movie> list) {
        if (list != null) {
            yourRecommendationsList = list;
            return true;
        }
        return false;
    }

    /**
     * Changes searchMovieList on search
     * @param list new list of movies from search query
     * @return boolean if list can be used to replace movie search list
     */
    public static boolean setSearchMovieList(List<Movie> list) {
        if (list != null) {
            searchMovieList = list;
            return true;
        }
        return false;
    }

    /**
     * Indicates whether newMoviesList has been set
     * @return True if newMoviesList not null, false otherwise
     */
    public static boolean hasNewMoviesList() {
        return newMoviesList != null;
    }

    /**
     * Indicates whether topRentalsList has been set
     * @return True if topRentalsList not null, false otherwise
     */
    public static boolean hasTopRentalsList() {
        return topRentalsList != null;
    }

    /**
     * Indicates whether yourRecommendationsList has been set
     * @return True if yourRecommendationsList not null, false otherwise
     */
    public static boolean hasYourRecommendationsList() {
        return yourRecommendationsList != null;
    }

    /**
     * Sets current tab
     * @param position position of current tab
     */
    public static void setTabPosition(int position) {
        currentTab = position;
        Log.e("GTMovies: setTab", position + " : " + (position % 3));
    }


    /**
     * Sets fragment to display search
     */
    public void setSearch() {
        search = true;
    }

    /**
     * Sets fragment to display tabs
     */
    public static void setTabs() {
        tabs = true;
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
                            intent.putExtra(MovieDetailFragment.ARG_ITEM_TITLE,
                                    holder.mMovieInfo.getTitle());
                            intent.putExtra(MovieDetailFragment.ARG_ITEM_ID,
                                    holder.mMovieInfo.getID());
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
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_TITLE,
                                holder.mMovieInfo.getTitle());
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID,
                                holder.mMovieInfo.getID());
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
            if (movieList != null) {
                return movieList.size();
            } else {
                return -1;
            }
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


///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
/////////////////////////JINU LOOK HERE////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
/*Comments for Jinu
    Whenever setting a tab/search use one of :

    MovieListFragment.setNewMoviesList(list);
    MovieListFragment.setTopRentalsList(list);
    MovieListFragment.setYourRecommendationsList(list);
    MovieListFragment.setSearchMovieList(list);



    Whenever using MovieListFragment, do:

    MovieListFragment movieListFragment = MovieListFragment.newInstance(page);

    the page value can be:
    MovieListFragment.NEW_MOVIES_TAB
    MovieListFragment.TOP_RENTALS_TAB
    MovieListFragment.YOUR_RENTALS_TAB
    MovieListFragment.SEARCH


    Last, but certainly not least, we need to update th UI.
    fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        movieListFragment).commit();




    Search may get weird. In which case, I will need to look more into setting currenttab
    Actually, performance may take a hit and we may be able to do movieListFragment with those
    tabs that are at the top that I'm not currently using rather than creating a new fragment each
    time.

    In the case that search does not work properly, we need to try to place this on the line before
    creating the new fragment instance:

    MovieListFragment.setTabPosition(page);

    with the page values I mentioned above. Likely will not work though.


    So, putting it all together, I will provide the following example:

    MovieListFragment.setNewMoviesList(list);
    MovieListFragment movieListFragment = MovieListFragment.newInstance(
            MovieListFragment.NEW_MOVIES_TAB);
    fragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        movieListFragment).commit();




 */
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
