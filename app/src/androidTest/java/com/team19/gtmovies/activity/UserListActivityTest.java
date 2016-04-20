/*
package com.team19.gtmovies.activity;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.exception.IllegalUserException;
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
import org.mockito.internal.matchers.Not;
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
import static com.team19.gtmovies.data.IOActions.getUserByUsername;
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

*/
/**
 * Created by matt on 4/3/16.
 * tests populateList in UserListActivity
 *//*


//@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    private static User u0 = null;
    private static User u1 = null;
    private static User u2 = null;
    private static User u3 = null;
    private static ArrayList<String> testNames = null;

    */
/** this line is preferred way to hook up to activity *//*

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

        //fake users
        u0 = new User ("clown", "passw", "");
        u1 = new User("john", "pass", "Bob");
        u2 = new User("kanye", "pass", "Mark");
        u3 = new User("DOESNT_EXIST", "pass", "");
        //create mock user with invalid permission
        User badPerm = null;
        try {
            badPerm = new User("badperm1","pass","Dawn");
            badPerm.setPermission(-2);
        } catch (IllegalUserException e) {
            Log.e("Error making fake user", e.getMessage());
        }
        //return custom badperm user
//        when(ioActions.getUserByUsername("badperm1")).thenReturn(badPerm);
        */
/*
        cannot do anything with fake users objects, method calls IOActions.getUserByUsername()
        to build new User with onfo form database.
         *//*


        testNames = new ArrayList<>(
                Arrays.asList(
                        u0.getUsername(),
                        u1.getUsername(),
                        u2.getUsername(),
                        u3.getUsername()
                )
        );
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



    @Test
    public void checkSetupSuccess() {
        //make sure testing objects are  setup right
        assertNotNull("UserListActivity is NULL",ula);
        assertNotNull("Sample list is NULL", testNames);
        assertEquals("Size is WRONG", testNames.size(), 4);
    }
    @Test
    public void checkListOmission() throws MissingMethodInvocationException,
            NotAMockException {
        //get result of sample names
        ArrayList<String> result = UserListActivity.formatList(testNames);
        //check results not null
        assertThat("Returned list is empty!", result.isEmpty(), is(false));
        //check result does not include names not in database
        assertEquals("list length is WRONG", 3, result.size());
    }

    @Test
    public void checkListFormatting() throws MissingMethodInvocationException,
            NotAMockException {
        //check functions that formatList relies on
        assertNotNull(getUserByUsername("kanye"));
        //get result of sample names
        ArrayList<String> result = UserListActivity.formatList(testNames);
        //check for proper formatting
        assertEquals(result.get(0), "[U]   clown");


        //get results of null list
        result = UserListActivity.formatList(null);
        Log.println(Log.INFO,"UserListActivityTest", result.toString()); //DEBUG
        //check null parameter return empty list rather than null, to prevent fail
        assertNotNull("function should not return null!", result);
    }

    @Test
    public void checkInvalidPermissionTest() throws MissingMethodInvocationException,
            NotAMockException {

        //get results for user with invalid permission
        ArrayList<String> result = UserListActivity.formatList(new ArrayList<String>(Arrays.asList("badperm1")));
        Log.println(Log.INFO,"UserListActivityTest", result.toString()); //DEBUG
        //assert list not null
        assertNotNull(result);
        //check formatting
        assertEquals("User with invalid permission should have '[U]' TAG!", true, result.get(0).contains("[U]"));
        */
/*
        invalid permissions should have [U] tag to keep formatting consistancy and presentation
         for end users. We can see bad data in our sql tables directly
         *//*

    }
}*/
