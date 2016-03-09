package com.team19.gtmovies.data;

import com.team19.gtmovies.pojo.User;

/**
 * Created by a on 2/22/2016.
 */
public class CurrentState {
    private static final CurrentState ourInstance = new CurrentState();
    private static User currentUser;

    public static CurrentState getInstance() {
        return ourInstance;
    }

    /**
     * private CurrentState constructor
     */
    private CurrentState() {
        currentUser = null;
    }

    /**
     * current user getter
     * @return returns current user
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * current user setter
     * @param user new user
     * @return if successful
     */
    public static boolean setUser(User user) {
        currentUser = user;
        return true;
    }
}

