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
    HashSet<User> accounts;
    FileInputStream fileIn;
    FileOutputStream fileOut;
    ObjectInputStream objectIn;
    ObjectOutputStream objectOut;
    Context context;

    public IOActions(Context c) throws Exception {
        context = c;
        onCreate("accounts.data", c);

    }

    protected void onCreate(String filename,Context context) {
        try {
            fileIn = context.openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            accounts = (HashSet<User>) objectIn.readObject();
            objectIn.close();
            System.out.println(accounts);
        } catch (FileNotFoundException f) {
            accounts = new HashSet<>();
            Log.println(Log.INFO, "GTMovies", "Created new empty set for user accounts");
            Log.e("GTMovies", "FileNotFoundException: "+Log.getStackTraceString(f));
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

    public void addUser(User user)
            throws DuplicateUserException {
        if (accounts.contains(user)) {
            throw new DuplicateUserException();
        } else {
            accounts.add(user);
        }
    }

    public User getUserByName(String name)
            throws NullUserException {
        for (User u: accounts) {
            if (u.getName().equals(name))
                return u;
        }
        throw new NullUserException();
    }

}
