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

    private static final String AFILE = "ACCOUNTS.txt";
    private static final String UFILE = "USER.txt";
    private static final String MFILE = "MOVIES.txt";
    private static Set<User> accounts = null;
    private static Set<Movie> movies;

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
            loadAccounts();
            loadUser();
            loadMovies();
        } catch (FileNotFoundException f) {
            accounts = new HashSet<>();
            movies = new HashSet<>();
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
    protected static void loadAccounts()
            throws ClassNotFoundException, IOException {
        fileIn = ioaContext.openFileInput(AFILE);
        objectIn = new ObjectInputStream(fileIn);
        accounts = (HashSet<User>) objectIn.readObject();
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "ACCOUNTS loaded with: " + accounts);
    }

    /**
     * gets object from serialized file
     * @throws ClassNotFoundException if error on readObject()
     * @throws IOException if error opening file
     */
    protected static void loadMovies()
            throws ClassNotFoundException, IOException {
        fileIn = ioaContext.openFileInput(MFILE);
        objectIn = new ObjectInputStream(fileIn);
        movies = (HashSet<Movie>) objectIn.readObject();
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "MOVIES loaded with: " + movies);
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
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "USER loaded with: " + CurrentState.getUser());
    }

    /**
     * serializes and writes HashSet accounts object
     * @throws IOException if fails to write out
     */
    public static void saveAccounts()
            throws IOException {
        fileOut = ioaContext.openFileOutput(AFILE, Context.MODE_PRIVATE);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(accounts);
        objectOut.close();
        Log.println(Log.INFO, "GTMovies", AFILE + " saved.");
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
     * serializes and writes HashSet movies object
     * @throws IOException if fails to write out
     */
    public static void saveMovies()
            throws IOException {
        fileOut = ioaContext.openFileOutput(MFILE, Context.MODE_PRIVATE);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(movies);
        objectOut.close();
        Log.println(Log.INFO, "GTMovies", MFILE + " saved.");
    }

    /**
     * save changes to USER and ACCOUNTS
     */
    protected static boolean commit() {
        try {
            saveAccounts();
            saveUser();
            saveMovies();
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
     * gets accounts
     * @return list of accounts
     */
    public static Set<User> getAccounts() {
        return accounts;
    }
    /**
     * gets accounts
     * @return list of accounts
     */
    public static ArrayList<String> getUsernames() {
        ArrayList<String> temp = new ArrayList<>(accounts.size());
        for (User u : accounts) {
            temp.add(u.getUsername());
        }
        return temp;
    }
    /**
     * gets movies
     * @return HashSet of movies
     */
    public static Set<Movie> getMovies() {
        return movies;
    }
    /**
     * add new user
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String result = reader.readLine();
            if(result.charAt(0) == 0) {
                throw new DuplicateUserException();
            } else {
                accounts.add(user);
                Log.println(Log.INFO, "GTMovies", "New user created! (" + user.getUsername() + ")");
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: adduser: error in decoding data: " + e.toString());
        }
//        if (accounts.contains(user) || user.getUsername().equals("null")) {
//            throw new DuplicateUserException();
//        } else {
//            accounts.add(user);
//            commit();
//            Log.println(Log.INFO, "GTMovies", "New user created! (" + user.getUsername() + ")");
//        }
    }

    /**
     * remove user from accounts
     * @param user User Object
     * @throws NullUserException if DNE
     */
    public static void deleteUser(User user) throws NullUserException {
        if (user == null)
            throw new NullUserException("User is null");

        String url = "http://45.55.175.68/test.php?mode=2&uname='" + user.getUsername() + "'";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: deleteUser: error in server connection: " + e.toString());
        }

        String uname = user.getUsername();
        accounts.remove(user);
        Log.println(Log.INFO, "GTMovies", "User '" + uname + "' deleted.");
        commit();
    }


    /**
     * log user in and commit changes
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
     * reset user to default null
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
     * removes old user from set and adds new user
     * @return
     */
    public static boolean updateUser() {
        User temp = CurrentState.getUser();

        String url = "http://45.55.175.68/test.php?mode=3&uname='" + temp.getUsername()
                + "'&pword='" + temp.getPassword()
                + "'&name='" + temp.getName()
                + "'&bio='" + temp.getBio()
                + "'&major='" + temp.getMajor()
                + "'&perm=" + temp.getPermission()
                + "&hasp=" + (temp.getHasProfile() ? 1 : 0);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: updateUser: error in server connection: " + e.toString());
        }

        accounts.remove(temp);
        accounts.add(temp);
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
     * if user exists return User object
     * else throw exception (it is expected that it will not fail)
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
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

//        for (User u: accounts) {
//            if (u.getUsername().equalsIgnoreCase(un))
//                return u;
//        }
//        return null;
    }


    /**
     * Save a new rating to both the respective user and local movie object
     * @param movieid ID from tomatoes
     * @param score score given by user
     * @param comment comment given by user
     * @return true if success
     */
    public static boolean SaveNewRating(int movieid, int score, String comment) {
        User ouruser = CurrentState.getUser();
        boolean success = true;
        Movie ourmovie = getMovieById(movieid);
        // Remove existing movie (if exists)
        if (ourmovie == null) {
            ourmovie = new Movie(movieid, 'c');
        } else {
            movies.remove(ourmovie);
        }
        // Add the new rating to each
        Review r = new Review(score, comment, ouruser.getUsername(), ourmovie.getID());
        try {
            ouruser.addReview(r);
            ourmovie.addReview(r);
        } catch (Exception e){
            Log.println(Log.ASSERT, "GTMovies", e.getMessage());
            success = false;
        }
        // Add movie back to hashset
        updateUser();
        movies.add(ourmovie);
        commit();
        return success;
    }
    /**
     * return Movie object for given id
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
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
                return m;
            }
        } catch (Exception e) {
            Log.e("GTMovies", "IOActions: getMovieByID: error in decoding data: " + e.toString());
        }
        return null;

//        Movie ourmovie = null;
//        // Find the movie for the given username;
//        try {
//            for(Movie m : movies) {
//                if(m.getID() == movieid) {
//                    ourmovie = m;
//                    break;
//                }
//            }
//        } catch(Exception e) {
//            Log.println(Log.ERROR, "GTMovies", e.getMessage());
//        }
//        return ourmovie;
    }
    /**
     * add new movie
     * @param movie movie object
     */
    public static void addMovie(Movie movie) {
        if(movies.contains(movie)) {
            throw new IllegalArgumentException("IOActions: this movie is already in the HashSet.");
        } else {
            movies.add(movie);
            Log.println(Log.INFO, "GTMovies", "New movie added! (" + movie.getID() + ")");
            commit();
        }
    }
    /**
     * remove movie from movies hashmap
     * @param movie Movie object
     */
    public static void deleteMovie(Movie movie) {
        if(movie == null) {
            throw new IllegalArgumentException("Can't remove null movie!");
        } else {
            int movid = movie.getID();
            movies.remove(movie);
            Log.println(Log.INFO, "GTMovies", "Movie '" + movid + "' deleted.");
            commit();
        }
    }

    /**
     * provides persistent MovieListFragment
     */
    public static void setMovieListFragment(MovieListFragment newList) {
        movieListFragment = newList;
    }
    /**
     * gets persistent MovieListFragment
     */
    public static MovieListFragment getMovieListFragment() {
        return movieListFragment;
    }

    /**
     * Getter for ioa actions
     * @return
     */
    public static IOActions getIOActionsInstance() {
        return ioa;
    }

}
