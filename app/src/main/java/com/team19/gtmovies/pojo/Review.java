package com.team19.gtmovies.pojo;

import com.team19.gtmovies.data.IOActions;

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
    private static final long SERIAL_VERSION_UID = 1L;

    /**
     * Constructor for the Review Class.
     * Creates a review for the
     * customer with a 0 score and an empty comment section.
     */
    public Review() {

        this(0, "", "", 0);
    }

    /**
     * Constructor for the Review Class.
     * Creates a review for the Customer with a complete score and a complete
     * comment.
     *
     * @param score1   the score the Customer gave the movie
     * @param comment1 the "review/comment" the user gave the movie.
     * @throws IllegalArgumentException if score > 5 or <0, if comment is
     *                                  null, or if username is null.
     */
    public Review(int score1, String comment1, String username1, int movieID1) {
        if (score1 < 0 || score1 > 5) {
            throw new IllegalArgumentException("Score is not within valid range.");
        } else if (comment1 == null) {
            throw new IllegalArgumentException("A null comment was passed.");
        } else if (username1 == null) {
            throw new IllegalArgumentException("A null username was passed.");
        }
        this.score = score1;
        this.comment = comment1;
        this.username = username1;
        this.movieID = movieID1;
    }

    /**
     * Method returns the score recorded in this review.
     *
     * @return score the review's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Method returns the comment associated with this review.
     *
     * @return comment the review's comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Method updates the score associated with this review.
     *
     * @param newScore the new score the Customer wants to give to this review.
     */
    public void setScore(int newScore) {
        score = newScore;
    }

    /**
     * Method updates the comment associated with this review.
     *
     * @param newComment the new comment the Customer wants to asscoxciate with this rating.
     */
    public void setComment(String newComment) {
        comment = newComment;
    }

    /**
     * Method returns the username recorded in this review.
     *
     * @return score the review's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method returns the movie ID recorded in this review.
     *
     * @return score the review's movie ID
     */
    public int getMovieID() {
        return movieID;
    }

    /**
     * Method updates the username associated with this review.
     *
     * @param username1 the new username to associate with this rating.
     */
    public void setUsername(String username1) {
        this.username = username1;
    }

    /**
     * Method updates the movie ID associated with this review.
     *
     * @param movieID1 the new movie ID to associate with this rating.
     */
    public void setMovieID(int movieID1) {
        this.movieID = movieID1;
    }

    /**
     * Checks if review is valid
     * Codes:
     * 1 - valid
     * -1 - null
     * -2 - movieID bad
     * -3 - movie bad
     * -4 - username bad
     * -5 - user bad
     * -6 - score bad
     *
     *
     * @param review review to check
     * @return 1 if good, negative integer code if bad.
     */
    public static int reviewIsValid(Review review) {
        if (review == null) {
            return -1;
        }

        //Check attached Movie
        if (review.movieID < 0) {
            return -2;
        } else {
            Movie mMovie = IOActions.getMovieById(review.movieID);
            if (mMovie == null
                || mMovie.getID() != review.movieID
                || mMovie.getTitle() == null
                || mMovie.getTitle().equals("")) {
                return -3;
            }
        }

        //Check attached user
        if (review.username == null || review.username.equals("")) {
            return -4;
        } else {
            User mUser = IOActions.getUserByUsername(review.username);
            if (mUser == null
                    || mUser.getUsername() == null
                    || mUser.getUsername().equals("")
                    || !mUser.getUsername().equals(review.username)) {
                return -5;
            }
        }

        //Check score
        if (review.score < 0 || review.score > 5) {
            return -6;
        }
        return 1;
    }

}
