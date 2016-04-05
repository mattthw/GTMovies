package com.team19.gtmovies.activity;

import com.team19.gtmovies.R;
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


import java.util.ArrayList;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.team19.gtmovies.data.CurrentState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
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
@RunWith(AndroidJUnit4.class)
//@RunWith(MockitoJUnitRunner.class)
public class UserListActivityTest {

    private static User u0 = null;
    private static User u1 = null;
    private static User u2 = null;
    private static User u3 = null;
    private static ArrayList<String> testNames = null;

    @Before
    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        mockActivity = Mockito.mock(UserListActivity.class);
        u0 = new User ("oiwhdowaif", "passw", "");
        u1 = new User("bob94", "pass", "Bob");
        u2 = new User("dogsrule1", "pass", "Mark");
        u3 = new User("NULL", "pass", "");

        u0.setPermission(-1);
        u1.setPermission(2);
        u3.setPermission(0);

        u2.setBio("SUP GUYS?");

        testNames = spy(new ArrayList<>(
                Arrays.asList(
                        u0.getUsername(),
                        u1.getUsername(),
                        u2.getUsername(),
                        u3.getUsername()
                )
        ));

    }

    /** this line is preferred way to hook up to activity */
    @Rule
    public ActivityTestRule<UserListActivity> mActivityRule = new ActivityTestRule<>(UserListActivity.class);

    @Test
    public void testFormatting() {
        ArrayList<String> res;
        res = mActivityRule.getActivity().formatList(testNames);
        assertThat(res.size(), is(res.size() - 1));
    }
    @InjectMocks
    CurrentState currState = Mockito.mock(CurrentState.class);
    @Mock
    IOActions ioActions = Mockito.mock(IOActions.class);
    @Mock
    User user = Mockito.mock(User.class);

    @Test
    public void checkFormatList() throws MissingMethodInvocationException,
            NotAMockException {
//        when(currState.getUser().getUsername()).thenReturn("Bob");
        currState.setUser(u1);
//        User tmp = currState.getUser();
        doReturn(u1).when(currState).getUser();
        doReturn(testNames).when(ioActions).getUsernames();
        when(ioActions.getUsernames()).thenReturn(testNames);
//        when(currState.getUser()).thenReturn(u1);
//        verify(mockActivity).populateList();
//        p("populateList() is called.");
//        verify(mockActivity).formatList(testNames);
        //check that it formatted u0 properly
//        onView(withId(R.id.userListView))
//            .check(matches(
//                    withText(
//                            containsString("[B]   " + "")
//                    )
//            ));
//        when(mockActivity.formatList(testNames)).thenReturn(testNames);
//        ArrayList<String> result = mockActivity.formatList(testNames);
//        assertThat(result.size(), is(result.size() - 1));
//        assertEquals(result, new ArrayList<String>(0));
    }
}