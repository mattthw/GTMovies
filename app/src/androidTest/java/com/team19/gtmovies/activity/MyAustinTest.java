/*
package com.team19.gtmovies.activity;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.team19.gtmovies.R;
import com.team19.gtmovies.pojo.User;

import org.junit.Before;
import org.junit.Test;

*/
/**
 * Created by a on 4/7/2016.
 *//*

public class MyAustinTest
        extends ActivityInstrumentationTestCase2<UserProfileActivity> {
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////!!!!!!!!I GOT IT WORKING!!!!!!!!!!!//////////////////
    /////////////////////////THIS IS THE CORRECT CLASS NOW//////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private UserProfileActivity mActivity;
    private User mUser;

    public MyAustinTest() {
        super(UserProfileActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Looper.prepare();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        assertNotNull(mActivity);
        try {
            mUser = new User("austinTest", "pass", "name");
        } catch (Exception e) {
            System.out.println("Could not instantiate mUser.");
            System.exit(0);
        }
        mActivity.setCurrentUser(mUser);
        assertNotNull(mActivity.getCurrentUser());
    }

    @Test
    public void testWithEspresso() {
        assertEquals("Initial permission not 1.", 1, mUser.getPermission());
        mActivity.changeRank(mActivity.findViewById(R.id.rankView));
        assertEquals("New permission not 2.", 2, mUser.getPermission());
        for (int i = 0; i < 6; i++) {
            mActivity.changeRank(mActivity.findViewById(R.id.rankView));
            assertEquals("New permission not " + ((i % 4) - 1) + ".",
                    (i % 4) - 1, mUser.getPermission());
        }

        mUser.setPermission(1);
        mUser.setPermission(-2);
        assertEquals("Error bounds in User's setPosition should prevent"
                + " other cases, but do not.", 1, mUser.getPermission());
        mUser.setPermission(3);
        assertEquals("Error bounds in User's getPermission should prevent"
                + " other cases, but do not", 1, mUser.getPermission());
    }
}
*/
