//package com.team19.gtmovies;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.team19.gtmovies.activity.MovieDetailActivity;
//import com.team19.gtmovies.fragment.MovieDetailFragment;
//import com.team19.gtmovies.fragment.MovieListFragment;
//import com.team19.gtmovies.pojo.MovieInfo;
//
//import java.util.List;
//
///**
// * Created by a on 2/22/2016.
// * @author Austin Leal
// * @version 1.0
// */
//public class MovieRecyclerViewAdapterOld
//        extends RecyclerView.Adapter<MovieRecyclerViewAdapterOld.MovieViewHolder> {
//    private List<MovieInfo> movieList;
//
//    /**
//     * inner class for RecyclerView.ViewHolder
//     */
//    public class MovieViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final ImageView mMoviePosterView;
//        public final TextView mMovieTitleView;
//        public final TextView mMovieRatingView;
//        public final TextView mMovieDescriptionView;
//        public MovieInfo mMovieInfo;
//
//        public MovieViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//            mMoviePosterView = (ImageView) itemView.findViewById(R.id.movie_poster);
//            mMovieTitleView = (TextView) itemView.findViewById(R.id.movie_title);
//            mMovieRatingView = (TextView) itemView.findViewById(R.id.movie_rating);
//            mMovieDescriptionView = (TextView) itemView.findViewById(R.id.movie_description);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mMovieTitleView.getText() + "'";
//        }
//    }
//
//    public MovieRecyclerViewAdapterOld(List<MovieInfo> movieInfo) {
//        movieList = movieInfo;
//    }
//
//    @Override
//    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.movie_list_content, parent, false);
//        return new MovieViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final MovieRecyclerViewAdapterOld.MovieViewHolder holder, int position) {
//        holder.mMovieInfo = movieList.get(position);
//        //holder.poster.setImageResource(mMovieInfo.poster);
//        if (holder != null) {
//            holder.mMovieTitleView.setText(holder.mMovieInfo.title);
//            String rating = holder.mMovieInfo.rating + "%";
//            holder.mMovieRatingView.setText(rating);
//            holder.mMovieDescriptionView.setText(holder.mMovieInfo.description);
//        } else {
//            Log.e("GTMovies", "No holder.");
//        }
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (MovieListFragment.isTwoPane()) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mMovieInfo.title);
//                    MovieDetailFragment fragment = new MovieDetailFragment();
//                    fragment.setArguments(arguments);
//                    /*this.getContext().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.movie_detail_container, fragment)
//                            .commit();*/
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, MovieDetailActivity.class);
//                    intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mMovieInfo.title);
//
//                    context.startActivity(intent);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return movieList.size();
//    }
//}
