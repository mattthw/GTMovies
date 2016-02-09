package com.team19.gtmovies;

import android.content.Context;
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
public class IOActions {
    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Context context;

    private final String FNAME = "ACCOUNTS.txt";
    protected static HashSet<User> accounts;
    protected static User currentUser;

    public IOActions(Context c) throws Exception {
        context = c;
        onCreate(FNAME, c);

    }

    protected void onCreate(String fn, Context context) {
        try {
            fileIn = context.openFileInput(fn);
            objectIn = new ObjectInputStream(fileIn);
            accounts = (HashSet<User>) objectIn.readObject();
            objectIn.close();
            Log.println(Log.INFO, "GTMovies", "ACCOUNTS: " + accounts);
        } catch (FileNotFoundException f) {
            accounts = new HashSet<>();
            this.commit();
            Log.println(Log.INFO, "GTMovies", "Created new empty set for user accounts");
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
    }

    protected void onClose(String filename, Context context) {
        try {
            fileOut = context.openFileOutput(filename, context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(accounts);
            objectOut.close();
        } catch (FileNotFoundException f) {
            Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
    }
    protected void commit() {
        try {
            fileOut = context.openFileOutput(FNAME, Context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(accounts);
            objectOut.close();
            Log.println(Log.INFO, "GTMovies", FNAME + " saved.");
        } catch (FileNotFoundException f) {
            Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
        } catch (IOException i) {
            Log.e("GTMovies", "IOException: "+Log.getStackTraceString(i));
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }
    }

    public HashSet<User> getAccounts() {
        return this.accounts;
    }
    public void addUser(User user)
            throws DuplicateUserException {
        if (accounts.contains(user)) {
            throw new DuplicateUserException();
        } else {
            accounts.add(user);
            Log.println(Log.INFO, "GTMovies", "New user created! (" + user.getUsername() + ")");
            this.commit();
        }
    }

    public User getUserByUsername(String un)
            throws NullUserException {
        for (User u: accounts) {
//            Log.println(Log.INFO, "GTMovies", "u: " + u.getUsername())
            if (u.getUsername().equals(un))
                return u;
        }
        throw new NullUserException();
    }

    /**
     * logs user in to GTMovies
     * @param username username
     * @param password password
     * @return User object if valid,null if invalid
     * @throws NullUserException if user does not exist
     */
    public User loginUser(String username, String password)
            throws NullUserException {
        User u = this.getUserByUsername(username);
        if (u.getPassword().equals(password)) {
            this.currentUser = u;
            Log.println(Log.INFO, "GTMovies", u.getUsername() + " signed in.");
            return u;
        }
        return null;
    }
}
