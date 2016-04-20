/*
package com.team19.gtmovies;

import android.support.test.runner.AndroidJUnit4;

import com.team19.gtmovies.exception.IllegalUserException;
import com.team19.gtmovies.exception.NullUserException;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;

*/
/**
 * Created by Jim Jang on 2016-04-04.
 *//*

@RunWith(AndroidJUnit4.class)
public class MovieGetUserRatingByMajorTest extends TestCase {
    private Movie noReview;
    private Movie nullUserReview;
    private Movie nullOtherMajor;
    private Movie allCorrect;
    private Movie allCorrectZero;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // EnvE major "clown"
        Review correct01 = new Review(
                5, "I leave ze comment", "clown", 9999);
        // CE major "john"
        Review correct02 = new Review(
                1, "Zis is comment iz zis not", "john", 9999);
        // Econ major "willy"
        Review correct03 = new Review(
                2, "I like little children covered in chocolate", "willy", 9999);

        // CS majors
        Review correctCS01 = new Review (
                4, "Honolulu, Hawaii", "JinuSexy123", 9999);
        Review correctCS02 = new Review (
                3, "CSTest02", "test2", 9999);
        Review correctCS03 = new Review (
                5, "CSTest03", "test3", 9999);

        // No majors
        Review otherMajorNull = new Review (
                4, "I'm a NeeGaa in Paris", "kayne", 9999);

        nullOtherMajor = new Movie(9999, "nullOtherMajorMovie", 4);
        nullOtherMajor.addReview(otherMajorNull);
        nullOtherMajor.addReview(correct01);

        noReview = new Movie(9999, "No Reviews Made", 5);

        allCorrect = new Movie(9999, "Everything is Correct", 4);
        allCorrect.addReview(correctCS01);
        allCorrect.addReview(correctCS02);
        allCorrect.addReview(correctCS03);

        allCorrectZero = new Movie(9999, "UserCount: Zero", 2);
        allCorrectZero.addReview(correct01);
        allCorrectZero.addReview(correct02);
        allCorrectZero.addReview(correct03);

    }

    @Test(expected = IllegalArgumentException.class)
    public void nullArgument() {
        assertEquals(-1, allCorrect.getUserRatingByMajor(null));
    }

    @Test (expected = IllegalArgumentException.class)
    public void path1_UserNull() throws IllegalArgumentException {
        // null User
        Review nullUser = new Review (
                2, "This should break everything", null, 9999);

        nullUserReview = new Movie(9999, "NullUserMovie", 4);
        nullUserReview.addReview(nullUser);

        int count = nullUserReview.getUserRatingByMajor("CS");
        assertEquals(-1, count);
    }

    @Test
    public void path2_OtherMajorNull() {
        // User != null
        // otherMajor == null -> got to be logged in with kanye
        int csCount = nullOtherMajor.getUserRatingByMajor("CS");
        assertEquals(-1, csCount);
    }

    @Test
    public void path3_NoReview() {
        int count = noReview.getUserRatingByMajor("CS");
        assertEquals(-1, count);
    }

    @Test
    public void path4_WrongMajor() {
        int count = allCorrect.getUserRatingByMajor("Random Unexistant Major");
        assertEquals(-1, count);
    }

    // Same expected result as correctPath_ZeroCount
//    @Test
//    public void path3_MajorNotEqual() {
//        // User != null
//        // otherMajor != null
//        // !major.equals(otherMajor)
//    }

    @Test
    public void correctPath_IntCount() {
        int correctScore = allCorrect.getUserRatingByMajor("CS");
        assertEquals(80, correctScore);
    }

    @Test
    public void correctPath_ZeroCount() {
        int zeroCount = allCorrectZero.getUserRatingByMajor("CS");
        assertEquals(-1, zeroCount);
    }
}
*/
