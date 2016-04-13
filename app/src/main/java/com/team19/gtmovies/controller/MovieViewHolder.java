package com.team19.gtmovies.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.team19.gtmovies.R;
import com.team19.gtmovies.pojo.Movie;

/**
 * RecyclerView holder for Movie Items
 *
 * @author Austin Leal
 * @version 2.0
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
     *
     * @param mItemView current view
     */
    public MovieViewHolder(View mItemView) {
        super(mItemView);

        Log.d("MLFrag", "create new MovieViewHolder");

        mView = mItemView;
        mMoviePosterView = (NetworkImageView) mItemView.findViewById(R.id.movie_poster);
        mMoviePosterView.setDefaultImageResId(R.mipmap.slowpoke);
        mMoviePosterView.setErrorImageResId(R.mipmap.load_error3);
        mMovieTitleView = (TextView) mItemView.findViewById(R.id.movie_title);
        mMovieRatingView = (TextView) mItemView.findViewById(R.id.movie_rating);
        mMovieDescriptionView = (TextView) mItemView.findViewById(R.id.movie_description);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mMovieTitleView.getText() + "'";
    }

    public Movie getMovieInfo() {
        return mMovieInfo;
    }
}

