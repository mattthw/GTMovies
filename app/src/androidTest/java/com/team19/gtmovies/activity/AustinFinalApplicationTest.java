package com.team19.gtmovies.activity;

import android.app.Application;
import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;
import com.team19.gtmovies.pojo.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;

/**
 * @author Austin Leal
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public class AustinFinalApplicationTest extends ApplicationTestCase<Application> {
    public AustinFinalApplicationTest() {
        super(Application.class);
    }

    @Spy IOActions ioa;


    @Spy private Review nullReview;
    @Spy private Review defaultWorstReview;
    @Spy private Review manualWorstReview;
    @Spy private Review badIdReview;
    @Spy private Review badTitleMovieReview;
    @Spy private Review nullUsernameReview;
    @Spy private Review badUsernameReview;
    @Spy private Review noUserReview;
    @Spy private Review badLowScoreReview;
    @Spy private Review badHighScoreReview;
    @Spy private Review validReview;
    @Spy public Review validNoCommentReview;


    @Before
    public void setUp() {
        Looper.prepare();
        ioa = IOActions.getIOActionsInstance();

        Movie goodMovie = new Movie(10156, "Title", 5);
        Movie badTitleMovie = new Movie(10156, "", 5);

        User goodUser;
        User nullUsernameUser;
        User noUsernameUser;
        User noReviewUser;

        try {
            goodUser = new User("Username", "password", "name");
            nullUsernameUser = new User("name", "password", "name");
            noUsernameUser = new User("name", "password", "name");
            noReviewUser = new User("Username", "password", "name");
        } catch (Exception e) {
            System.out.println("ERROR! Users were not created properly.");
            System.exit(0);
            goodUser = null;
            nullUsernameUser = null;
            noUsernameUser = null;
            noReviewUser = null;
        }

        nullUsernameUser.setUsername(null);
        noUsernameUser.setUsername("");

        try {
            ioa.addUser(goodUser);
            ioa.addUser(nullUsernameUser);
            ioa.addUser(noUsernameUser);
            ioa.addUser(noReviewUser);
        } catch (Exception e) {
            System.out.println(e);
        }

        nullReview = null;
        defaultWorstReview = new Review();
        manualWorstReview = new Review(0, "", "", 1);
        manualWorstReview.setMovieID(-1);             //After testing, let's
                                                    //let's make this not work.
        badIdReview = new Review(5, "Comment", goodUser.getUsername(),
                goodMovie.getID());
        badIdReview.setMovieID(-1);
        badUsernameReview = new Review(5, "Comment",
                noUsernameUser.getUsername(), goodMovie.getID());
        noUserReview = new Review(5, "Comment", "unknown username",
                goodMovie.getID());
        badLowScoreReview = new Review(0, "Comment", goodUser.getUsername(),
                goodMovie.getID());
        badLowScoreReview.setScore(-1);
        badHighScoreReview = new Review(5, "Comment", goodUser.getUsername(),
                goodMovie.getID());
        badHighScoreReview.setScore(6);


        validReview = new Review(5, "Comment", goodUser.getUsername(),
                goodMovie.getID());
        goodUser.addReview(validReview);
        validNoCommentReview = new Review(5, "", goodUser.getUsername(),
                goodMovie.getID());

        CurrentState.setUser(goodUser);

        ioa.updateUser(goodUser);
        ioa.saveNewRating(validReview.getMovieID(), validReview.getScore(),
                validReview.getComment());
    }

    @Test
    public void reviewIsValid() {
        //Branch 1
        assertEquals("Null review is not returning correct code",
                -1, Review.reviewIsValid(null));
        assertEquals("Bad review created by non-default constructor is not"
                + "returning correct code",
                -2, Review.reviewIsValid(manualWorstReview));
        assertEquals("Bad review created with default constructor is not"
                + " returning correct code",
                -3, Review.reviewIsValid(defaultWorstReview));
        assertEquals("Bad Movie ID Review is not returning correct code",
                -2, Review.reviewIsValid(badIdReview));
        assertEquals("Bad username Review is not returning correct code",
                -4, Review.reviewIsValid(badUsernameReview));
        assertEquals("Not found user is not returning correct code",
                -5, Review.reviewIsValid(noUserReview));
        assertEquals("Score that's too low not returning correct code",
                -6, Review.reviewIsValid(badLowScoreReview));
        assertEquals("Score that's too high not returning correct code",
                -6, Review.reviewIsValid(badHighScoreReview));
        assertEquals("Proper review is not returning correct code",
                1, Review.reviewIsValid(validReview));
        assertEquals("Proper review with no comment is not returning correct"
                + "code", 1, Review.reviewIsValid(validNoCommentReview));
    }
}