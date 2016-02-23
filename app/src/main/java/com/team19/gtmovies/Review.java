package com.team19.gtmovies;

import java.io.Serializable;

/**
 * Created by ericmartin827 on 2/23/16.
 * A review is specific to the user who produced it and the movie that
 * was rated. Each review will also know the score the score the user gave it
 * and the comment the user produced.
 */
public class Review implements Serializable {

    private String userName;
    private int score;
    private int movieID;
    private String comment;

    /**
     * Constructor for the Review Class.
     * Creates a review for the
     * customer with a 0 score and an empty comment section.
     * @param userName the customer's userName
     * @param movieID the id of the move being rated
     */
    public Review(String userName, int movieID) {
        this(userName, movieID, 0, "");
    }

    /**
     * Constructor for the Review Class.
     * Creates a review for the Customer with a complete score and a complete
     * commnet.
     * @param userName the customer's userName
     * @param movieID the id of the move being rated
     * @param score the score the Customer gave the movie
     * @param comment the "review/comment" the user gave the movie.
     */
    public Review(String userName, int movieID, int score, String comment) {
        this.userName = userName;
        this.movieID = movieID;
        this.score = score;
        this.comment = comment;
    }

    /**
     *
     * Method returns the userName of the customer who generated this review.
     * @return userName the Customer's userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * Method returns the movieID of the movie that was reviewed by a Customer.
     * @return movieId the identification number of the movie asscociated with this
     *          review
     */
    public int getMovieID() {
        return movieID;
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
     * Method determines if two reviews are equivalent. A review is idetincal
     * if it has the came userName and movieID .
     * @return a boolean determining if two reviews are equivalent.
     */
    public boolean equals(Review otherReview) {
        return userName.equals(otherReview.userName) && movieID == otherReview.movieID;
    }

    /**
     * Method generates a hash value for this Reeview.
     * @return the hashCode of this review.
     */
    public int hashCode() {
        String hashString = userName.concat(Integer.toString(movieID));
        return hashString.hashCode();
    }
}
