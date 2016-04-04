package com.team19.gtmovies.activity;

import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.User;



import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;

import java.util.ArrayList;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by matt on 4/3/16.
 * tests populateList in UserListActivity
 */
@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    private static User u0 = null;
    private static User u1 = null;
    private static User u2 = null;
    private static User u3 = null;
    private static ArrayList<String> testNames = null;

    private UserListActivity mActivity;
    @Before
    public void setUp() throws Exception {
        mActivity = new UserListActivity();

        User u0 = new User ("oiwhdowaif", "passw", "");
        User u1 = new User("bob94", "pass", "Bob");
        User u2 = new User("dogsrule1", "pass", "Mark");
        User u3 = new User("NULL", "pass", "");

        u0.setPermission(-1);
        u1.setPermission(2);
        u3.setPermission(0);

        u2.setBio("SUP GUYS?");

        testNames = new ArrayList<>(
                Arrays.asList(
                        u0.getUsername(),
                        u1.getUsername(),
                        u2.getUsername(),
                        u3.getUsername()
                )
        );


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPopulateList() throws Exception {
//        when(IOActions.getUsernames()).thenReturn(testNames);
//        when(CurrentState.getUser().getUsername()).thenReturn(u1.getUsername());

//        ArrayList<String> postOpUsernames =
    }
}