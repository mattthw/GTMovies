package com.team19.gtmovies.pojo;

import java.io.Serializable;

/**
 * Created by ericmartin827 on 2/23/16.
 * A review is specific to the user who produced it and the movie that
 * was rated. Each review will also know the score the score the user gave it
 * and the comment the user produced.
 */
public class Review implements Serializable {

    private int score;
    private String comment;
    private String username;
    private int movieID;
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the Review Class.
     * Creates a review for the
     * customer with a 0 score and an empty commen  t section.
     */
    public Review() {
        this(0, "", "", 0);
    }

    /**
     * Constructor for the Review Class.
     * Creates a review for the Customer with a complete score and a complete
     * comment.
     * @param score the score the Customer gave the movie
     * @param comment the "review/comment" the user gave the movie.
     */
    public Review(int score, String comment, String username, int movieID) {
        this.score = score;
        this.comment = comment;
        this.username = username;
        this.movieID = movieID;
    }

    /**
     * Method returns the score recorded in this review.
     * @return score the review's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Method returns the comment associated with this review.
     * @return comment the review's comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Method updates the score associated with this review.
     * @param newScore the new score the Customer wants to give to this review.
     */
    public void setScore(int newScore) {
        score = newScore;
    }

    /**
     * Method updates the comment associated with this review.
     * @param newComment the new comment the Customer wants to asscoxciate with this rating.
     */
    public void setComment(String newComment) {
        comment = newComment;
    }

    /**
     * Method returns the username recorded in this review.
     * @return score the review's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method returns the movie ID recorded in this review.
     * @return score the review's movie ID
     */
    public int getMovieID() {
        return movieID;
    }

    /**
     * Method updates the username associated with this review.
     * @param username the new username to associate with this rating.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method updates the movie ID associated with this review.
     * @param movieID the new movie ID to associate with this rating.
     */
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
}
