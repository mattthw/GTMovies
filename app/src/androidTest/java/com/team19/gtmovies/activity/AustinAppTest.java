package com.team19.gtmovies.activity;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.test.ApplicationTestCase;
import android.test.mock.MockApplication;
import android.view.View;

import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.pojo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

//import org.powermock.core.
//import static org.powermock.api.mockito.PowerMockito.method;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Austin Leal
 * @version 1.0
 */
//@RunWith(AndroidJUnit4.class)
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(UserProfileActivity.class)
public class AustinAppTest extends ApplicationTestCase<Application> {
    public AustinAppTest() {
        super(Application.class);
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //////////////THIS TEST WAS ABANDONED BECAUSE I COULDN'T GET////////////////
    //////////////POWERMOCK IN THE GRADLE WITHOUT REDUNDANCIES//////////////////
    //////////////THE CRIPPLED THE WHOLE APP////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Rule
    public ActivityTestRule<UserProfileActivity> jerry = null;
    @Rule
    public ActivityTestRule<CurrentState> cState = null;
    @Spy User mUser;
    @Mock UserListActivity ula;
    //@Spy CurrentState spyState;


    Mockito mockito;
    MockApplication mockApp;
    //UserProfileActivity mUserProfileActivity;
    @Mock View view;
    @Spy UserProfileActivity tom;
    ViewInteraction mView;


    @Mock CurrentState cState2;
    @Spy CurrentState mCurrentState;
    @InjectMocks
    UserProfileActivity danny;
    //@InjectMocks
    //UserProfileActivity tom;

    //ViewInteraction mView;

    /**
     * Setup things needed for Navigation
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //Looper.prepare();
        mockito = new Mockito();
        mockApp = new MockApplication();
        jerry = new ActivityTestRule<>(UserProfileActivity.class);
        cState = new ActivityTestRule<>(CurrentState.class);
        InstrumentationRegistry.getContext();
        //mockApp.onCreate();
        //context = mock(Context.class);
        //mainActivity = mockito.mock(MainActivity.class);
        //view = Mockito.mock(View.class);
        //view.setLabelFor(R.id.rankView);
        //mView = Espresso.onView(ViewMatchers.withId(R.id.rankView));
        //UiSelector mUiSelector = new UiSelector();
        //mView2 = UiObject.findObject(mUiSelector.withId(R.id.rankView));
        try {
            mUser = new User("austinTESTTEST", "pass", "name");
        } catch (Exception e) {
            System.out.println("THERE IS A PROBLEM");
        }
        //cState.getActivity().setUser(mUser);
        //cState2.setUser(mUser);
        //spyState = Mockito.mock(CurrentState.class);
        //spyState.setUser(mUser);
        //CurrentState.setUser(mUser);
        //danny = new UserProfileActivity();
        //mCurrentState = new CurrentState(mUser);
        //mUser = Mockito.mock(User.class);
        mUser.setPermission(1);
        mCurrentState = Mockito.mock(CurrentState.class);
        mCurrentState.setUser(mUser);
        tom = Mockito.mock(UserProfileActivity.class);
        tom = new UserProfileActivity();


        //Mockito.doNothing().when((AppCompatActivity) mainActivity);
        //UserProfileActivity bob = new UserProfileActivity();
        //mUserProfileActivity.onCreate(bundle);
        MockitoAnnotations.initMocks(this);
        tom.setCurrentUser(mUser);
    }

    /**
     * Test selecting an item from the application navigation drawer.
     * @throws Exception
     */
    @Test
    public void testChangeRank() throws Exception {
        //assertEquals("CurrentState user incorrect", mUser,
        //      cState.getActivity().getUser());
        assertEquals("User permission not initially 1", 1,
                mUser.getPermission());
        //Looper.prepare();
        //jerry = Mockito.mock(UserProfileActivity.class);
        //assertNotEquals("not equal", 1.0, mUser.getPermission() * (1.0));

        //bob2 = (UserProfileActivity) UserProfileActivity.getStaticInstance();
        //AppCompatActivity appCompatActivity = mockito.mock(
        //      AppCompatActivity.class);


        //doNothing().when(tom, method(UserProfileActivity.class,
        //        "updateRank")).withNoArguments();
        //PowerMockito.doNothing().when((AppCompatActivity) danny).onCreate(
        //      new Bundle());
        //String result = UserProfileActivity.testSomethingStatic(view, danny);
        //Scanner mScanner = new Scanner(result);
        //mScanner.useDelimiter("/");
        //jerry.getActivity().changeRank(view);
        //danny.changeRank(view);
        //((UserProfileActivity) bob2).changeRank(view);
        //tom.changeRank(view);


        for (int i = 1; i < 6; i++) {
            //assertEquals("Permission not changed correctly", (i % 4) - 1,
            // mScanner.next());
        }
        //mUserProfileActivity.changeRank(view);
        //assertEquals("not ok", -1, mCurrentState.getUser().getPermission());
        //assertEquals(1,2);

        assertNotNull("mUser null", mUser);
        tom.setCurrentUser(mUser);
        for (int i = -1; i < 2; i++) {
            assertNotNull("tom's current user null", tom.getCurrentUser());
            tom.getCurrentUser().setPermission(i);
            //tom.setCurrentUser(mUser);
            //CurrentState.setUser(mUser);
            //tom = new UserProfileActivity();
            tom.changeRank(view);
            assertEquals("Permission not plus 1 , given i=" + i, i + 1,
                    tom.getCurrentUser().getPermission());
        }

        mUser.setPermission(2);
        tom.changeRank(view);
        assertEquals("Permission not 2 -> -1", -1,
                mCurrentState.getUser().getPermission());

        /* NOTE: checking other values is senseless since they should be checked
         * in User's setPermission() method, so that UserProfileActivity's
         * changeRank method never sees a permission that is not -1, 0, 1, or 2.
         *
         * In the following usage, Permission is expected to stay at 1, so
         * changeRank is expected to change it to 2
         */
        mUser.setPermission(1);
        mUser.setPermission(5);
        tom.changeRank(view);
        assertEquals("Permission not 2 -> -1", 2,
                mCurrentState.getUser().getPermission());
    }

}