package com.team19.gtmovies.data;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.team19.gtmovies.exception.DuplicateUserException;
import com.team19.gtmovies.exception.NullUserException;
import com.team19.gtmovies.fragment.MovieListFragment;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;
import com.team19.gtmovies.pojo.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//import com.team19.gtmovies.pojo.User;


/**
 * Created by matt on 2/8/16.
 * used for data retrieval etc
 * @author Matt McCoy
 * @version 2.0
 */
public class IOActions extends Application {
    private static IOActions ioa = null;
    private static MovieListFragment movieListFragment = null;
    private static FileInputStream fileIn;
    private static FileOutputStream fileOut;
    private static ObjectInputStream objectIn;
    private static ObjectOutputStream objectOut;
    private static Context ioaContext;

    private static final String UFILE = "USER.txt";

    /**
     * constructor for IOActions
     * @param c context passed by calling class
     */
    public IOActions(Context c)  {
        if (ioa == null) {
            ioaContext = c;
            onStart();
            if (userSignedIn()) {
                Log.println(Log.ASSERT, "GTMovies", CurrentState.getUser().getUsername() + " signed in.");
            } else {
                CurrentState.setUser(null);
//            Log.println(Log.ASSERT, "GTMovies", "no user signed in.");
            }
            ioa = this;
        }
    }

    /**
     * begin process of loading accounts and users
     */
    protected static void onStart() {
        try {
            loadUser();
        } catch (FileNotFoundException f) {
            CurrentState.setUser(null);
            commit();
            Log.println(Log.INFO, "GTMovies", "Created new empty set for user accounts");
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
    }

    /**
     * gets object from serialized file
     * @throws ClassNotFoundException if error on readObject()
     * @throws IOException if error opening file
     */
    protected static void loadUser()
            throws ClassNotFoundException, IOException {
        fileIn = ioaContext.openFileInput(UFILE);
        objectIn = new ObjectInputStream(fileIn);
        CurrentState.setUser((User) objectIn.readObject());
        if(CurrentState.getUser() != null) {
            CurrentState.setUser(IOActions.getUserByUsername(CurrentState.getUser().getUsername()));
        }
        Log.d("GTMovies", "loaduser: " + CurrentState.getUser().toString());
        commit();
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "USER loaded with: " + CurrentState.getUser());
    }

    /**
     * serializes and writes User object
     * @throws IOException if fails to write object
     */
    public static void saveUser()
            throws IOException {
        fileOut = ioaContext.openFileOutput(UFILE, Context.MODE_PRIVATE);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(CurrentState.getUser());
        objectOut.close();
        Log.println(Log.INFO, "GTMovies", UFILE + " saved with user: "
                + CurrentState.getUser().getUsername() + " "
                + CurrentState.getUser().getMajor());
    }

    /**
     * save changes to USER and ACCOUNTS
     * EDIT: After implementing database this only saves
     * the currentUser to file.
     * everything else is stored remotely.
     * @return true, because we never fail
     */
    protected static boolean commit() {
        try {
            saveUser();
        } catch (FileNotFoundException f) {
            Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
        return true;
    }

    /**
     * gets accounts from mysql database on remote server
     * @return list of usernames from database
     */
    public static ArrayList<String> getUsernames() {
        String url = "http://45.55.175.68/test.php?mode=7";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getUsernames: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            StringTokenizer st = new StringTokenizer(result, "\\", false);
            result = st.nextToken();
            if(result.charAt(0) == '0') {
                return new ArrayList<String>();
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                Log.d("GTMovies Database", "GetUsernames: " + result);
                temp.add(result);
                while(st.hasMoreTokens()) {
                    result = st.nextToken();
                    Log.d("GTMovies Database", "GetUsernames: " + result);
                    temp.add(result);
                }
                return temp;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getUsernames: error in decoding data: " + e.toString());
        }
        //returned if exceptions prevent return of valid list
        return new ArrayList<String>();
    }
    /**
     * gets rated movies from database
     * @return Set of Movie objects created with information
     *      from the database.
     */
    public static Set<Movie> getMovies() {
        String url = "http://45.55.175.68/test.php?mode=8";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getMovies: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            StringTokenizer st = new StringTokenizer(result, "\\", false);
            result = st.nextToken();
            if(result.charAt(0) == '0') {
                return new HashSet<Movie>();
            } else {
                HashSet<Movie> temp = new HashSet<Movie>();
                int id = Integer.parseInt(result);
                String title = st.nextToken();
                result = st.nextToken(); // to rating
                int rating = Integer.parseInt(result);
                Log.d("GTMovies Database", "GetMovies: " + new Movie(id, title, rating));
                temp.add(new Movie(id, title, rating));
                while(st.hasMoreTokens()) {
                    result = st.nextToken();
                    id = Integer.parseInt(result);
                    title = st.nextToken(); // to title
                    result = st.nextToken(); // to rating
                    rating = Integer.parseInt(result);
                    Log.d("GTMovies Database", "GetMovies: " + result);
                    temp.add(new Movie(id, title, rating));
                }
                return temp;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getMovies: error in decoding data: " + e.toString());
        }
        return new HashSet<Movie>();
    }
    /**
     * add new user to the database
     * @param user user object
     * @throws DuplicateUserException
     */

    public static void addUser(User user)
            throws DuplicateUserException, NullUserException {
        if(user.getUsername().equals("null")) {
            throw new DuplicateUserException();
        }
        String url = "http://45.55.175.68/test.php?mode=1&uname='" + user.getUsername()
                + "'&pword='" + user.getPassword()
                + "'&name='" + user.getName()
                + "'&bio='" + user.getBio()
                + "'&major='" + user.getMajor()
                + "'&perm=" + user.getPermission()
                + "&hasp=" + (user.getHasProfile() ? 1 : 0);
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: addUser: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            if(result.charAt(0) == '0') {
                throw new DuplicateUserException();
            } else {
                Log.println(Log.INFO, "GTMovies", "New user created! (" + user.getUsername() + ")");
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: adduser: error in decoding data: " + e.toString());
        }
    }

    /**
     * remove user from database
     * @param user User Object
     * @throws NullUserException if DNE
     */
    public static void deleteUser(User user) throws NullUserException {
        if (user == null){
            throw new NullUserException("User is null");
        }

        String url = "http://45.55.175.68/test.php?mode=2&uname='" + user.getUsername() + "'";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: deleteUser: error in server connection: " + e.toString());
        }

        String uname = user.getUsername();
        Log.println(Log.INFO, "GTMovies", "User '" + uname + "' deleted.");
        commit();
    }


    /**
     * log user in and commit changes to current state
     * @param uname username
     * @param p password
     * @return true if success
     */
    public static boolean loginUser(String uname, String p)
        throws NullUserException {
        User temp = getUserByUsername(uname);
        if (temp == null) {
            throw new NullUserException();
        }
        Log.println(Log.DEBUG, "GTMovies", "param: " + uname + ":" + p
                + "\ntemp: " + temp.getUsername() + ":"
                + "probably shouldn't be logging people's passwords");
        if (temp.correctPassword(p)) {
            CurrentState.setUser(temp);
            Log.println(Log.ASSERT, "GTMovies",
                    "'" + temp.getUsername() + "' signed in.");
            commit();
            return true;
        } else {
            Log.println(Log.ASSERT, "GTMovies",
                    "sign in attempt of '" + temp.getUsername() + "' failed.");
            return false;
        }
    }
    /**
     * reset user to default null and commit changes to current state
     * @return true if CurrentState.getUser() set
     */
    public static boolean logoutUser() {
        if (CurrentState.getUser() != null) {
            Log.println(Log.ASSERT, "GTMovies", CurrentState.getUser().getUsername() + " signed out.");
            CurrentState.setUser(null);
            commit();
            return true;
        }
        return false;
    }

    /**
     * updates a user in the database
     * @return true if completed without exception
     */
    public static boolean updateUser(User temp) {

        String url = "http://45.55.175.68/test.php?mode=3&uname='" + temp.getUsername()
                + "'&pword='" + temp.getPassword()
                + "'&name='" + temp.getName()
                + "'&bio='" + temp.getBio()
                + "'&major='" + temp.getMajor()
                + "'&perm=" + temp.getPermission()
                + "&hasp=" + (temp.getHasProfile() ? 1 : 0);
        try {
            url = url.replace(" ", "%20");
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: updateUser: error in server connection: " + e.toString());
            return false;
        }

        return true;
    }
    /**
     * gets sign in status
     * @return  true if user is currently signed in
     */
    public static boolean userSignedIn() {
        return !(CurrentState.getUser() == null);
    }

    /**
     * if a user exists in database, make and return a user object
     * else return null (it is expected that it will not fail)
     * @param un username
     * @return User
     */
    public static User getUserByUsername(String un) {
        String url = "http://45.55.175.68/test.php?mode=4&uname='" + un + "'";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getUserByUsername: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            StringTokenizer st = new StringTokenizer(result, "\\", false);
            result = st.nextToken();
            if(result.charAt(0) == '0') {
                return null;
            } else {
                User u = new User();
                u.setUsername(result);
                Log.d("GTMovies Database", "GetUserByUsername: Username: " + result);
                result = st.nextToken(); // change to password
                u.setPassword(result);
                Log.d("GTMovies Database", "GetUserByUsername: Password: " + result);
                result = st.nextToken(); // change to name
                u.setName(result);
                Log.d("GTMovies Database", "GetUserByUsername: Name: " + result);
                result = st.nextToken(); // change to bio
                u.setBio(result);
                Log.d("GTMovies Database", "GetUserByUsername: Bio: " + result);
                result = st.nextToken(); // change to major
                u.setMajor(result);
                Log.d("GTMovies Database", "GetUserByUsername: Major: " + result);
                result = st.nextToken(); // change to permission
                u.setPermission(Integer.parseInt(result));
                result = st.nextToken(); // change to hasProfile
                if(Integer.parseInt(result) == 1) {
                    u.setHasProfile(true);
                } else {
                    u.setHasProfile(false);
                }
                return u;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getUserByUsername: error in decoding data: " + e.toString());
        }
        return null;
    }


    /**
     * Save a new rating to database
     * @param movieid ID from tomatoes
     * @param score score given by user
     * @param comment comment given by user
     * @return true if success
     */
    public static boolean saveNewRating(int movieid, int score, String comment) {
        if(comment.equals("")) {
            comment = "no comment";
        }
        String url = "http://45.55.175.68/test.php?mode=6&movid=" + movieid
                + "&uname='" + CurrentState.getUser().getUsername()
                + "'&score=" + score
                + "&comm='" + comment + "'";
        InputStream is = null;
        try {
            url = url.replace(" ", "%20");
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: SaveNewRating: error in server connection: " + e.toString());
            return false;
        }
        return true;
    }
    /**
     * return Movie object for given id from database
     * @param movieid id form tomato API
     * @return Movie object with matching ID
     */
    public static Movie getMovieById(int movieid) {
        String url = "http://45.55.175.68/test.php?mode=5&movid=" + movieid;
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getMovieByID: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            StringTokenizer st = new StringTokenizer(result, "\\", false);
            result = st.nextToken();
            if(result.charAt(0) == '0') {
                return null;
            } else {
                Movie m = new Movie(0, '0');
                m.setId(Integer.parseInt(result));
                Log.d("GTMovies Database", "GetMovieById: Movie ID: " + result);
                result = st.nextToken(); // change to title
                m.setTitle(result);
                Log.d("GTMovies Database", "GetMovieById: Movie Title: " + result);
                result = st.nextToken(); // change to rating
                m.setRating(Integer.parseInt(result));
                Log.d("GTMovies Database", "GetMovieById: Movie Rating: " + result);
                m.setReviews(); //set reviews form DB
                return m;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getMovieByID: error in decoding data: " + e.toString());
        }
        //if failed
        return null;
    }

    /**
     * get an arraylist of movie reviews for a given movie ID
     * @param movieid the movie ID to get reviews for
     * @return an arraylist of movie reviews corresponding to movieid
     */
    public static ArrayList<Review> getListMovieReviews(int movieid) {
        String url = "http://45.55.175.68/test.php?mode=10&movid=" + movieid;
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getListMovieReviews: error in server connection: " + e.toString());
        }

        try {
            final int junk = 8;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), junk);
            String result = reader.readLine();
            StringTokenizer st = new StringTokenizer(result, "\\", false);
            result = st.nextToken();
            if(result.charAt(0) == '0') {
                return new ArrayList<Review>();
            } else {
                ArrayList<Review> temp = new ArrayList<Review>();
                String uname = result;
                result = st.nextToken(); // to movieid
                int movid = Integer.parseInt(result);
                result = st.nextToken(); // to score
                int score = Integer.parseInt(result);
                String comm = st.nextToken(); // to comment
                Log.d("GTMovies Database", "getListMovieReviews: " + new Review(score, comm, uname, movid));
                temp.add(new Review(score, comm, uname, movid));
                while(st.hasMoreTokens()) {
                    uname = st.nextToken();
                    result = st.nextToken(); // to movieid
                    movid = Integer.parseInt(result);
                    result = st.nextToken(); // to score
                    score = Integer.parseInt(result);
                    comm = st.nextToken();
                    Log.d("GTMovies Database", "getListMovieReviews: " + new Review(score, comm, uname, movid));
                    temp.add(new Review(score, comm, uname, movid));
                }
                return temp;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getListMovieReviews: error in decoding data: " + e.toString());
        }
        //return empty if failed
        return new ArrayList<Review>();
    }

    /**
     * provides persistent MovieListFragment
     * @param newList MovieListFragment to set
     */
    public static void setMovieListFragment(MovieListFragment newList) {
        movieListFragment = newList;
    }
    /**
     * gets persistent MovieListFragment
     * @return the fragment
     */
    public static MovieListFragment getMovieListFragment() {
        return movieListFragment;
    }

    /**
     * Getter for ioa actions
     * @return the current IOActions state
     */
    public static IOActions getIOActionsInstance() {
        return ioa;
    }

}
