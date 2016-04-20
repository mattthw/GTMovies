/*
package com.team19.gtmovies;

import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;
import com.team19.gtmovies.pojo.User;

import org.mockito.Mockito;

//import org.junit.Before;
//import org.junit.Test;


*/
/**
 * @author Austin Leal
 * @version 1.0
 *//*

public class WRONGAustinUnitTest{

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///IGNORE THIS ONE, let Jinu's be the correct getUserRatingByMajor test/////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///IGNORE THIS ONE, let Jinu's be the correct getUserRatingByMajor test/////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///IGNORE THIS ONE, let Jinu's be the correct getUserRatingByMajor test/////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///IGNORE THIS ONE, let Jinu's be the correct getUserRatingByMajor test/////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private static String major1;
    private static String major2;
    private static String invalidMajor;

    private static User noMajorUser;
    User major1GoodUser1;
    User major1GoodUser2;
    User major1BadUser;
    User major1UserNoReviews;
    User major2User;

    private static Movie movie1;
    Movie movie2;

    private static Review movie1NoMajorReview;
    Review movie1Major1GoodUser1Review;
    Review movie1Major1GoodUser2Review;
    Review movie1Major1BadUserReview;
    Review movie1Major2Review;
    Review movie1InvalidUserReview;

    Review movie2NoMajorReview;
    Review movie2Major1GoodUser1Review;
    Review movie2Major1GoodUser2Review;
    Review movie2Major1BadUserReview;
    Review movie2Major2Review;
    Review movie2InvalidUserReview;
    Review invalidReview;

    int movie1NoMajorTotal;
    int movie1Major1Total;
    int movie1Major2Total;

    int movie2Total;

    */
/**
     * Setup things needed for Navigation
     * @throws Exception
     *//*

    //@Before
    public void setUp() throws Exception {
        Mockito context = new Mockito();

        major1 = "CE";
        major2 = "ME";
        invalidMajor = "Notamajor";

        noMajorUser = new User("AUSTINTESTNoMajor", "NoMajor", "No Major");
        major1GoodUser1 = new User("AUSTINTESTMajor1-1", "Major1", "Major 1-1");
        major1GoodUser2 = new User("AUSTINTESTMajor1-2", "Major1", "Major 1-2");
        major1BadUser = new User("AUSTINTESTMajor1-3", "Major1", "Major 1-3");
        major1UserNoReviews = new User("AUSTINTESTMajor1-NoReviews", "Major1",
                "Major 1-NoReviews");
        major2User = new User("AUSTINTESTMajor2", "Major2", "Major 2");

        major1GoodUser1.setMajor(major1);
        major1GoodUser2.setMajor(major1);
        major1BadUser.setMajor(major1);
        major1UserNoReviews.setMajor(major1);
        major2User.setMajor(major2);

        IOActions.addUser(noMajorUser);
        IOActions.addUser(major1GoodUser1);
        IOActions.addUser(major1GoodUser2);
        IOActions.addUser(major1BadUser);
        IOActions.addUser(major1UserNoReviews);
        IOActions.addUser(major2User);


        movie1 = new Movie(123456789, "AUSTINTESTMOVIE1", 100);
        movie2 = new Movie(12345678, "AUSTINTESTMOVIE2", 0);

        movie1NoMajorReview = new Review(90, "AUSTINTESTREVIEW1",
                noMajorUser.getUsername(), movie1.getID());

        if (movie1NoMajorReview == null) {
            System.out.println("90 " + "REview1 " + noMajorUser.getUsername()
                    + movie1.getID());
        }
        movie1Major1GoodUser1Review = new Review(91, "AUSTINTESTREVIEW2",
                major1GoodUser1.getUsername(), movie1.getID());
        movie1Major1GoodUser2Review = new Review(92, "AUSTINTESTREVIEW3",
                major1GoodUser2.getUsername(), movie1.getID());
        movie1Major1BadUserReview = new Review(30, "AUSTINTESTREVIEW4",
                major1BadUser.getUsername(), movie1.getID());
        movie1Major2Review = new Review(93, "AUSTINTESTREVIEW5",
                major2User.getUsername(), movie1.getID());
        movie1InvalidUserReview = new Review(94, "AUSTINTESTREVIEW6",
                "Invalid Username", movie1.getID());

        movie1NoMajorTotal = 90;
        movie1Major1Total = (91+ 92 + 30)/2;
        movie1Major2Total = 93;

        movie2NoMajorReview = new Review(0, "AUSTINTESTREVIEW7",
                noMajorUser.getUsername(), movie2.getID());
        movie2Major1GoodUser1Review = new Review(0, "AUSTINTESTREVIEW8",
                major1GoodUser1.getUsername(), movie2.getID());
        movie2Major1GoodUser2Review = new Review(0, "AUSTINTESTREVIEW9",
                major1GoodUser2.getUsername(), movie2.getID());
        movie2Major1BadUserReview = new Review(0, "AUSTINTESTREVIEW10",
                major1BadUser.getUsername(), movie2.getID());
        movie2Major2Review = new Review(0, "AUSTINTESTREVIEW11",
                major2User.getUsername(), movie2.getID());
        movie2InvalidUserReview = new Review(0, "AUSTINTESTREVIEW12",
                "Invalid Username", movie2.getID());
        movie2Total = 0;

        invalidReview = new Review(0, "AUSTINTESTREVIEW12",
                major2User.getUsername(), 123);

*/
/*        movie1.addReview(movie1NoMajorReview);
        movie1.addReview(movie1Major1GoodUser1Review);
        movie1.addReview(movie1Major1GoodUser2Review);
        movie1.addReview(movie1Major1BadUserReview);
        movie1.addReview(movie1Major2Review);
        movie1.addReview(movie1InvalidUserReview);

        movie2.addReview(movie2NoMajorReview);
        movie2.addReview(movie2Major1GoodUser1Review);
        movie2.addReview(movie2Major1GoodUser2Review);
        movie2.addReview(movie2Major1BadUserReview);
        movie2.addReview(movie2Major2Review);
        movie2.addReview(movie2InvalidUserReview);*//*


        //IOActions
    }

    */
/**
     * Test selecting an item from the application navigation drawer.
     * @throws Exception
     *//*

    //@Test
    public void testGetUserReviewsByMajor() throws Exception {
        */
/*assertEquals("Movie1 no major not correct",
                movie1.getUserRatingByMajor(""), movie1NoMajorTotal);
        assertEquals("Movie1 Major1 not correct",
                movie1.getUserRatingByMajor(major1), movie1Major1Total);
        assertEquals("Movie1 Major2 not correct",
                movie1.getUserRatingByMajor(major2), movie1Major2Total);
        assertEquals("Movie1 invalid major",
                movie1.getUserRatingByMajor(invalidMajor), -1);

        //Checks that movie1 not interfering with movie2
        assertEquals("Movie2 no major not correct",
                movie1.getUserRatingByMajor(""), movie2Total);
        assertEquals("Movie2 Major1 not correct",
                movie1.getUserRatingByMajor(major1), movie2Total);
        assertEquals("Movie2 Major2 not correct",
                movie1.getUserRatingByMajor(major2), movie2Total);
        assertEquals("Movie2 invalid major",
                movie1.getUserRatingByMajor(invalidMajor), -1);*//*

    }
}*/
