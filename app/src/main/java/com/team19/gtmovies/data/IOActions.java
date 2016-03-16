package com.team19.gtmovies.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.team19.gtmovies.exception.DuplicateUserException;
import com.team19.gtmovies.exception.NullUserException;
import com.team19.gtmovies.pojo.Movie;
import com.team19.gtmovies.pojo.Review;
import com.team19.gtmovies.pojo.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

//import com.team19.gtmovies.pojo.User;


/**
 * Created by matt on 2/8/16.
 * used for data retrieval etc
 * @author Matt McCoy
 * @version 2.0
 */
public class IOActions extends Application {
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
        ioaContext = c;
        onStart();
        if (userSignedIn()) {
            Log.println(Log.ASSERT, "GTMovies", CurrentState.getUser().getUsername() + " signed in.");
        } else {
            CurrentState.setUser(new User());
//            Log.println(Log.ASSERT, "GTMovies", "no user signed in.");
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
            CurrentState.setUser(new User());
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
    protected static void commit() {
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
    }

    /**
     * gets accounts
     * @return HashSet of accounts
     */
    public static Set<User> getAccounts() {
        return accounts;
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
        if (accounts.contains(user) || user.getUsername().equals("null")) {
            throw new DuplicateUserException();
        } else {
            accounts.add(user);
            Log.println(Log.INFO, "GTMovies", "New user created! (" + user.getUsername() + ")");
            commit();
        }
    }
    /**
     * remove user from accounts
     * @param user User Object
     * @throws NullUserException if DNE
     */
    public static void deleteUser(User user) throws NullUserException {
        if (user == null)
            throw new NullUserException("User is null");
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
        Log.println(Log.ASSERT, "GTMovies", CurrentState.getUser().getUsername() + " signed out.");
        CurrentState.setUser(new User());
        commit();
        return true;
    }
    public static boolean updateUser() {
        User temp = CurrentState.getUser();
        accounts.remove(temp);
        accounts.add(temp);
        return true;
    }
    /**
     * gets sign in status
     * @return  true if user is currently signed in
     */
    public static boolean userSignedIn() {
        return !(CurrentState.getUser() == null
                || CurrentState.getUser().getUsername().equals("null")
                || CurrentState.getUser().getUsername().equals("logged_out"));
    }
    /**
     * if user exists return User object
     * else throw exception (it is expected that it will not fail)
     * @param un username
     * @return User
     */
    public static User getUserByUsername(String un) {
        for (User u: accounts) {
            if (u.getUsername().equalsIgnoreCase(un))
                return u;
        }
        return null;
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
        Movie ourmovie = null;
        // Find the movie for the given username;
        try {
            for(Movie m : movies) {
                if(m.getID() == movieid) {
                    ourmovie = m;
                    break;
                }
            }
        } catch(Exception e) {
            Log.println(Log.ERROR, "GTMovies", e.getMessage());
        }
        return ourmovie;
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

}
