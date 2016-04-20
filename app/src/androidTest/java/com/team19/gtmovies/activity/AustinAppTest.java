/*
package com.team19.gtmovies.activity;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.pojo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//import org.powermock.core.
//import static org.powermock.api.mockito.PowerMockito.method;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;

*/
/**
 * @author Austin Leal
 * @version 1.0
 *//*

@RunWith(AndroidJUnit4.class)
public class AustinAppTest extends ApplicationTestCase<Application> {
    public AustinAppTest() {
        super(Application.class);
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //////////////THIS TEST WAS ABANDONED BECAUSE I COULDN'T GET////////////////
    //////////////POWERMOCK IN THE GRADLE WITHOUT REDUNDANCIES//////////////////
    //////////////THE CRIPPLED THE WHOLE APP.///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ///////////////IT WAS THEN RETURNED TO, BUT BEING UNABLE////////////////////
    ///////////////TO INSTANTIATE ASSETMANAGER PREVENTED SUCCCESS///////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Rule
    public ActivityTestRule<UserProfileActivity> jerry = null;
    @Rule
    public ActivityTestRule<CurrentState> cState = null;
    @Spy User mUser;
    @Mock UserListActivity ula;
    @Spy Context context;
    UserProfileActivity mUserProfileActivity;
    @Mock TextView view;
    @Spy UserProfileActivity tom;
    //ViewInteraction mView;
    @Mock UserProfileActivity bob;


    @Mock CurrentState cState2;
    @Spy CurrentState mCurrentState;
    //@InjectMocks
    //UserProfileActivity tom;
    @InjectMocks
    UserProfileActivity bill;

    //ViewInteraction mView;

    */
/**
     * Setup things needed for Navigation
     * @throws Exception
     *//*

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Looper.prepare();
        //mockito = new Mockito();
        Application mockApp = mock(Application.class);
        tom = new UserProfileActivity();
        jerry = new ActivityTestRule<>(UserProfileActivity.class);
        cState = new ActivityTestRule<>(CurrentState.class);
        //context = mock(Context.class);
        InstrumentationRegistry.getContext();
        context = getContext();
        */
/*final AssetManager assetManager = mock(AssetManager.class);
        doNothing().when(assetManager).close();
        doReturn(new String[10]).when(assetManager).getLocales();
        doReturn(new String[10]).when(assetManager).list(Mockito.anyString());
        doReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        }).when(assetManager).open(Mockito.anyString());
        doReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        }).when(assetManager).open(Mockito.anyString(), Mockito.anyInt());
        doReturn(mock(AssetFileDescriptor.class)).when(
                assetManager).openFd(Mockito.anyString());
        doReturn(mock(AssetFileDescriptor.class)).when(
                assetManager).openNonAssetFd(Mockito.anyString());
        doReturn(mock(AssetFileDescriptor.class)).when(
                assetManager).openNonAssetFd(0, Mockito.anyString());
        doReturn(mock(XmlResourceParser.class)).when(
                assetManager).openXmlResourceParser(Mockito.anyString());
        doReturn(mock(XmlResourceParser.class)).when(
                assetManager).openXmlResourceParser(0, Mockito.anyString());*//*



        */
/*Context mContext = new Context() {
            @Override
            public AssetManager getAssets() {
                return AustinAppTest.super.getSystemContext().getAssets();
            }

            @Override
            public Resources getResources() {
                return new Resources(getAssets(),
                        new DisplayMetrics(), new Configuration());
            }

            @Override
            public PackageManager getPackageManager() {
                return null;
            }

            @Override
            public ContentResolver getContentResolver() {
                return null;
            }

            @Override
            public Looper getMainLooper() {
                return null;
            }

            @Override
            public Context getApplicationContext() {
                return null;
            }

            @Override
            public void setTheme(int resid) {

            }

            @Override
            public Resources.Theme getTheme() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public String getPackageName() {
                return null;
            }

            @Override
            public ApplicationInfo getApplicationInfo() {
                return null;
            }

            @Override
            public String getPackageResourcePath() {
                return null;
            }

            @Override
            public String getPackageCodePath() {
                return null;
            }

            @Override
            public SharedPreferences getSharedPreferences(String name, int mode) {
                return null;
            }

            @Override
            public FileInputStream openFileInput(String name) throws FileNotFoundException {
                return null;
            }

            @Override
            public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
                return null;
            }

            @Override
            public boolean deleteFile(String name) {
                return false;
            }

            @Override
            public File getFileStreamPath(String name) {
                return null;
            }

            @Override
            public File getFilesDir() {
                return null;
            }

            @Override
            public File getNoBackupFilesDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalFilesDir(String type) {
                return null;
            }

            @Override
            public File[] getExternalFilesDirs(String type) {
                return new File[0];
            }

            @Override
            public File getObbDir() {
                return null;
            }

            @Override
            public File[] getObbDirs() {
                return new File[0];
            }

            @Override
            public File getCacheDir() {
                return null;
            }

            @Override
            public File getCodeCacheDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalCacheDir() {
                return null;
            }

            @Override
            public File[] getExternalCacheDirs() {
                return new File[0];
            }

            @Override
            public File[] getExternalMediaDirs() {
                return new File[0];
            }

            @Override
            public String[] fileList() {
                return new String[0];
            }

            @Override
            public File getDir(String name, int mode) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                return null;
            }

            @Override
            public boolean deleteDatabase(String name) {
                return false;
            }

            @Override
            public File getDatabasePath(String name) {
                return null;
            }

            @Override
            public String[] databaseList() {
                return new String[0];
            }

            @Override
            public Drawable getWallpaper() {
                return null;
            }

            @Override
            public Drawable peekWallpaper() {
                return null;
            }

            @Override
            public int getWallpaperDesiredMinimumWidth() {
                return 0;
            }

            @Override
            public int getWallpaperDesiredMinimumHeight() {
                return 0;
            }

            @Override
            public void setWallpaper(Bitmap bitmap) throws IOException {

            }

            @Override
            public void setWallpaper(InputStream data) throws IOException {

            }

            @Override
            public void clearWallpaper() throws IOException {

            }

            @Override
            public void startActivity(Intent intent) {

            }

            @Override
            public void startActivity(Intent intent, Bundle options) {

            }

            @Override
            public void startActivities(Intent[] intents) {

            }

            @Override
            public void startActivities(Intent[] intents, Bundle options) {

            }

            @Override
            public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

            }

            @Override
            public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {

            }

            @Override
            public void sendBroadcast(Intent intent) {

            }

            @Override
            public void sendBroadcast(Intent intent, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void sendStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void removeStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Override
            public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                return null;
            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
                return null;
            }

            @Override
            public void unregisterReceiver(BroadcastReceiver receiver) {

            }

            @Nullable
            @Override
            public ComponentName startService(Intent service) {
                return null;
            }

            @Override
            public boolean stopService(Intent service) {
                return false;
            }

            @Override
            public boolean bindService(Intent service, ServiceConnection conn, int flags) {
                return false;
            }

            @Override
            public void unbindService(ServiceConnection conn) {

            }

            @Override
            public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
                return false;
            }

            @Override
            public Object getSystemService(String name) {
                return null;
            }

            @Override
            public String getSystemServiceName(Class<?> serviceClass) {
                return null;
            }

            @Override
            public int checkPermission(String permission, int pid, int uid) {
                return 0;
            }

            @Override
            public int checkCallingPermission(String permission) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfPermission(String permission) {
                return 0;
            }

            @Override
            public int checkSelfPermission(String permission) {
                return 0;
            }

            @Override
            public void enforcePermission(String permission, int pid, int uid, String message) {

            }

            @Override
            public void enforceCallingPermission(String permission, String message) {

            }

            @Override
            public void enforceCallingOrSelfPermission(String permission, String message) {

            }

            @Override
            public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

            }

            @Override
            public void revokeUriPermission(Uri uri, int modeFlags) {

            }

            @Override
            public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
                return 0;
            }

            @Override
            public int checkCallingUriPermission(Uri uri, int modeFlags) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
                return 0;
            }

            @Override
            public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
                return 0;
            }

            @Override
            public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

            }

            @Override
            public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

            }

            @Override
            public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

            }

            @Override
            public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {

            }

            @Override
            public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createConfigurationContext(Configuration overrideConfiguration) {
                return null;
            }

            @Override
            public Context createDisplayContext(Display display) {
                return null;
            }
        };
        context = new ContextWrapper(context);
        Intent intent = AustinTestHelp.create(context);
        assertNotNull(intent);*//*

        context = new ContextWrapper(new MockContext());

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
        mCurrentState = Mockito.mock(CurrentState.class);
        mCurrentState.setUser(mUser);

        MockitoAnnotations.initMocks(this);

        //context = new ContextWrapper(mContext);
        assertNotNull(mockApp);
        //mockApp.startActivity(intent);

        assertNotNull(mUser);
        mUser.setPermission(1);
        verify(mUser).setPermission(Mockito.anyInt());
        */
/*
        tom = Mockito.mock(UserProfileActivity.class);
        tom = new UserProfileActivity();*//*

        assertNotNull(context);
        //assertNotNull(context.getResources());
        //assertNotNull(context.getResources().getDisplayMetrics());
        view = new TextView(getSystemContext());
        view.setId(R.id.rankView);

        //Mockito.doNothing().when((AppCompatActivity) mainActivity);
        //UserProfileActivity bob = new UserProfileActivity();
        //mUserProfileActivity.onCreate(bundle);
        //verify(bill).setCurrentUser(mUser);
        //verify(jerry.getActivity()).setCurrentUser(mUser);
        //verify(bob).setCurrentUser(mUser);
        verify(tom).setCurrentUser(mUser);
        tom.setCurrentUser(mUser);
    }

    */
/**
     * Test selecting an item from the application navigation drawer.
     * @throws Exception
     *//*

    @Test
    public void testChangeRank() throws Exception {
        //assertEquals("CurrentState user incorrect", mUser,
        //      cState.getActivity().getUser());
        assertEquals("User permission not initially 1", 1,
                mUser.getPermission());

        verify(tom).findViewById(Mockito.anyInt());
        doReturn(view).when(tom.findViewById(R.id.rankView)); //thenReturn(new TextView(context));

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

        */
/* NOTE: checking other values is senseless since they should be checked
         * in User's setPermission() method, so that UserProfileActivity's
         * changeRank method never sees a permission that is not -1, 0, 1, or 2.
         *
         * In the following usage, Permission is expected to stay at 1, so
         * changeRank is expected to change it to 2
         *//*

        mUser.setPermission(1);
        mUser.setPermission(5);
        tom.changeRank(view);
        assertEquals("Permission not 2 -> -1", 2,
                mCurrentState.getUser().getPermission());
    }

}*/
