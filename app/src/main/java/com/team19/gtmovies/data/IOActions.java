package com.team19.gtmovies.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.team19.gtmovies.CurrentState;
//import com.team19.gtmovies.pojo.User;
import com.team19.gtmovies.exception.DuplicateUserException;
import com.team19.gtmovies.exception.NullUserException;
import com.team19.gtmovies.pojo.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;


/**
 * Created by matt on 2/8/16.
 * used for data retrieval etc
 */
public class IOActions extends Application {
    private static FileInputStream fileIn;
    private static FileOutputStream fileOut;
    private static ObjectInputStream objectIn;
    private static ObjectOutputStream objectOut;
    private static Context ioaContext;

    private static final String AFILE = "ACCOUNTS.txt";
    private static final String UFILE = "USER.txt";
    private static HashSet<User> accounts;

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
        } catch (FileNotFoundException f) {
            accounts = new HashSet<>();
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
     * save changes to USER and ACCOUNTS
     */
    protected static void commit() {
        try {
            saveAccounts();
            saveUser();
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
    public static HashSet<User> getAccounts() {
        return accounts;
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
    }

    /**
     * gets sign in status
     * @return  true if user is currently signed in
     */
    public static boolean userSignedIn() {
        return !(CurrentState.getUser() == null || CurrentState.getUser().getUsername().equals("null"));
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
}
