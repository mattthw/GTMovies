package com.team19.gtmovies.pojo;
//import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.team19.gtmovies.pojo.Review;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Created by ericmartin827 on 4/4/16.
 * This test tests whether a Review Object can be safely generated using
 * the four argument constructor.
 */
public class ReviewTesterEric {

    private Review review;
    private String testComment;
    private String testUserName;
    private int validScore;
    private int invalidScoreHigh;
    private int invalidScoreLow;
    private int lowestScore;
    private int highestScore;
    private int testMovieID;

    @Before
    public void setUp() {
        testComment = "This Movie Rocks";
        testUserName = "EricMartin827";
        validScore = 3;
        invalidScoreHigh = 15;
        invalidScoreLow = -15;
        lowestScore = 0;
        highestScore = 5;
        testMovieID = 55;
    }


    @Test
    public void testContructorWithCorrectArguments() {
        review = new Review(validScore, testComment, testUserName, testMovieID);
        assertEquals(validScore, review.getScore());
        assertEquals(testComment, review.getComment());
        assertEquals(testUserName, review.getUsername());
        assertEquals(testMovieID, review.getMovieID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCommentConstructor() {
        review = new Review(validScore, null, testUserName, testMovieID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUserName() {
        review = new Review(validScore,testComment, null, testMovieID);
    }

    @Test
    public void testValidLowEdgeCase() {
        review = new Review(lowestScore, testComment, testUserName, testMovieID);
        assertEquals(lowestScore, review.getScore());
    }

    @Test
    public void testValidHighEdgeCase() {
        review = new Review(highestScore, testComment, testUserName, testMovieID);
        review = new Review(lowestScore, testComment, testUserName, testMovieID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHighScore() {
        review = new Review(invalidScoreHigh, testComment, testUserName, testMovieID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLowScore() {
        review = new Review(invalidScoreLow, testComment, testUserName, testMovieID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLowEdge() {
        review = new Review(lowestScore - 1, testComment, testUserName, testMovieID);
    }
    @Test(expected = IllegalArgumentException.class)
    public void getTestInvalidHighEdge() {
        review = new Review(highestScore + 1, testComment, testUserName, testMovieID);
    }


}
