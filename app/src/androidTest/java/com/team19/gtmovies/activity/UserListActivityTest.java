package com.team19.gtmovies.activity;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.pojo.User;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.support.test.runner.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.runners.MockitoJUnitRunner;
//import org.testng.annotations.BeforeMethod;


import android.support.test.rule.ActivityTestRule;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.team19.gtmovies.data.CurrentState.*;
import static com.team19.gtmovies.data.IOActions.getUsernames;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matt on 4/3/16.
 * tests populateList in UserListActivity
 */

//@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    private static User u0 = null;
    private static User u1 = null;
    private static User u2 = null;
    private static User u3 = null;
    private static ArrayList<String> testNames = null;

    /** this line is preferred way to hook up to activity */
    @Rule
    public ActivityTestRule<UserListActivity> mActivityRule = null;
    @Rule
    public ActivityTestRule<CurrentState> mCSRule = null;
    @InjectMocks
    CurrentState currState;
    @InjectMocks
    IOActions ioActions;
    @Mock
    User user;
    @Mock UserListActivity ula = null;

    @Before
    public void setUp() throws Exception {
        //rules
        mActivityRule = new ActivityTestRule<>(UserListActivity.class);
        mCSRule = new ActivityTestRule<>(CurrentState.class);
        InstrumentationRegistry.getContext();
        //mocks
        user = Mockito.mock(User.class);
        currState = Mockito.mock(CurrentState.class);
        ioActions = Mockito.mock(IOActions.class);
        ula = Mockito.mock(UserListActivity.class);
        MockitoAnnotations.initMocks(this);

        //fake things
        u0 = new User ("clown", "passw", "");
        u1 = new User("john", "pass", "Bob");
        u2 = new User("kanye", "pass", "Mark");
        u3 = new User("DOESNT_EXIST", "pass", "");

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
        //return u1 when getUser()
//        doReturn(u1).when(currState);
//        getUser();
        when(user.getUsername()).thenReturn("bob94");
//        //return custom list when getUSernames()
//        doReturn(testNames).when(ioActions);
//        getUsernames();
//        //return this string when getUsername()
//        doReturn("bob94").when(user);
//        user.getUsername();
//
//        //trying return custom user a different way
//        getUser();
//        Mockito.when((User)null).thenReturn(u1);



    }

//
//    @Test
//    public void testFormatting() {
//        ArrayList<String> res;
//        res = mActivityRule.getActivity().formatList(testNames);
//        assertThat(res.size(), is(res.size() - 1));
//    }


    @Test
    public void checkSetupSuccess() {
        //make sure testing objects are  setup right
        assertNotNull("UserListActivity is NULL",ula);
        assertNotNull("Sample list is NULL", testNames);
        assertEquals("Size is WRONG", testNames.size(), 4);
    }
    @Test
    public void checkFormatList() throws MissingMethodInvocationException,
            NotAMockException {
        //check functions that formatList relies on
        assertNotNull(IOActions.getUserByUsername("kanye"));
        //get result of sample names
        ArrayList<String> result = UserListActivity.formatList(testNames);
        //check results
        assertThat("Returned list is empty!", result.isEmpty(), is(false));
        assertEquals("list length is WRONG", 3, result.size());
//        Log.println(Log.INFO,"UserListActivityTest", testNames.toString()); //DEBUG
        assertEquals(result.get(0), "[U]   clown");
    }
}