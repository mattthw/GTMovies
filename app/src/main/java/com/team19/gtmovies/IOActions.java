package com.team19.gtmovies;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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
    protected static HashSet<User> accounts;
    protected static User currentUser;

    public IOActions(Context c)  {
        ioaContext = c;
        onStart();
        if (userSignedIn()) {
            Log.println(Log.ASSERT, "GTMovies", currentUser.getUsername() + " signed in.");
        } else {
            currentUser = new User();
//            Log.println(Log.ASSERT, "GTMovies", "no user signed in.");
        }
    }

    protected static void onStart() {
        try {
            loadAccounts();
            loadUser();
        } catch (FileNotFoundException f) {
            accounts = new HashSet<>();
            currentUser = new User();
            commit();
            Log.println(Log.INFO, "GTMovies", "Created new empty set for user accounts");
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
    }
    protected static void loadAccounts()
            throws ClassNotFoundException, IOException {
        fileIn = ioaContext.openFileInput(AFILE);
        objectIn = new ObjectInputStream(fileIn);
        accounts = (HashSet<User>) objectIn.readObject();
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "ACCOUNTS loaded with: " + accounts);
    }

    protected static void loadUser()
            throws ClassNotFoundException, IOException {
        fileIn = ioaContext.openFileInput(UFILE);
        objectIn = new ObjectInputStream(fileIn);
        currentUser = (User) objectIn.readObject();
        objectIn.close();
        Log.println(Log.DEBUG, "GTMovies", "USER loaded with: " + currentUser);
    }

    protected static void saveAccounts()
            throws ClassNotFoundException, IOException {
        fileOut = ioaContext.openFileOutput(AFILE, Context.MODE_PRIVATE);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(accounts);
        objectOut.close();
        Log.println(Log.INFO, "GTMovies", AFILE + " saved.");
    }

    protected static void saveUser()
            throws ClassNotFoundException, IOException {
        fileOut = ioaContext.openFileOutput(UFILE, Context.MODE_PRIVATE);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(currentUser);
        objectOut.close();
        Log.println(Log.INFO, "GTMovies", UFILE + " saved with user: " + currentUser.getUsername());
    }

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

    public static void deleteUser(User user) throws NullUserException {
        if (user == null)
            throw new NullUserException("User is null");
        String uname = user.getUsername();
        accounts.remove(user);
        Log.println(Log.INFO, "GTMovies", "User '" + uname + "' deleted.");
    }

    public static boolean userSignedIn() {
        if (currentUser == null || currentUser.getUsername().equals("null"))
            return false;
        return true;
    }

    public static User getUserByUsername(String un)
            throws NullUserException {
        for (User u: accounts) {
            if (u.getUsername().equals(un))
                return u;
        }
        throw new NullUserException();
    }

    /**
     * updates currentUser to user if their credentials match
     * @param uname user
     * @param p pass
     * @return true if success, false on failure
     * @throws NullUserException getUSerByUsername()
     */
    public static boolean loginUser(String uname, String p)
            throws NullUserException {
        User temp = getUserByUsername(uname);
        Log.println(Log.DEBUG, "GTMovies", "param: " + uname + ":" + p
            + "\ntemp: " + temp.getUsername() + ":" +temp.getPassword());
        if (temp.getPassword().equals(p)) {
            currentUser = temp;
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
     * @return true if currentUser set
     */
    public static boolean logoutUser() {
        Log.println(Log.ASSERT, "GTMovies", currentUser.getUsername() + " signed out.");
        currentUser = new User();
        commit();
        return true;
    }
}
