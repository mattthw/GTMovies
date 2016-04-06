package com.team19.gtmovies;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.team19.gtmovies.activity.UserProfileActivity;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.pojo.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * @author Austin Leal
 * @version 1.0
 */
public class CORRECTAustinUnitTest {
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /////////Let this be Austin's JUnit test for grading purposes///////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    UserProfileActivity mUserProfileActivity;
    View view;
    User mUser;

    /**
     * Setup things needed for Navigation
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Context context = Mockito.mock(Context.class);
        Bundle bundle = Mockito.mock(Bundle.class);
        view = Mockito.mock(View.class);
        mUser = new User("Austin's User", "pwd1", "user stuff");
        CurrentState.setUser(mUser);
        mUserProfileActivity = Mockito.mock(UserProfileActivity.class);
        UserProfileActivity bob = new UserProfileActivity();
        //mUserProfileActivity.onCreate(bundle);
    }

    /**
     * Test selecting an item from the application navigation drawer.
     * @throws Exception
     */
    @Test
    public void testGetUserReviewsByMajor() throws Exception {
        UserProfileActivity jerry = Mockito.mock(UserProfileActivity.class);
        //when(jerry.testSomething(view)).thenReturn("String");
        //System.out.println("this " + jerry.testSomething(view));

        //check each of first three
        int old = mUser.getPermission();
        System.out.println("old" + old);
        mUserProfileActivity.changeRank(view);
        assertEquals("not ok 1", old + 1, CurrentState.getUser().getPermission());
        mUserProfileActivity.changeRank(view);
        assertEquals("not ok", -1, CurrentState.getUser().getPermission());
        for (int i = -1; i < 2; i++){
            mUser.setPermission(i);
            mUserProfileActivity.changeRank(view);
            assertEquals("Permission not plus 1",
                    CurrentState.getUser().getPermission(), i + 1);
        }

        mUser.setPermission(2);
        mUserProfileActivity.changeRank(view);
        assertEquals("Permission not 2 -> -1",
                CurrentState.getUser().getPermission(), -1);

        /* NOTE: checking other values is senseless since they should be checked
         * in User's setPermission() method, so that UserProfileActivity's
         * changeRank method never sees a permission that is not -1, 0, 1, or 2.
         *
         * In the following usage, Permission is expected to stay at 1, so
         * changeRank is expected to change it to 2
         */
        mUser.setPermission(1);
        mUser.setPermission(5);
        mUserProfileActivity.changeRank(view);
        assertEquals("Permission not 2 -> -1",
                CurrentState.getUser().getPermission(), 2);
    }
}