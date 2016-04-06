package com.team19.gtmovies.data;

import com.team19.gtmovies.pojo.User;

/**
 * Created by Austin on 2/22/2016.
 * used by all other classes when requesting current user info
 * rgtrgrgtrs
 */
public class CurrentState {
    //    private static final CurrentState ourInstance = new CurrentState();
    private static User currentUser;
    private static int openHeight = 0;
    private static int closedHeight = 0;

//    public static CurrentState getInstance() {
//        return ourInstance;
//    }

    /**
     * default constructor for CurrentState
     *
     * @param u require the correct user as parameter as form of
     *          DEPENDENCY INJECTION. replaces singleton implementation.
     * @Matt 3/April/2016
     */
    public CurrentState(User u) {
        currentUser = u;
    }

    /**
     * current user getter
     *
     * @return returns current user
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * current user setter
     *
     * @param user new user
     * @return if successful
     */
    public static boolean setUser(User user) {
        currentUser = user;
        return true;
    }


    /**
     * Getter for height of viewpager with toolbar open
     *
     * @return open height
     */
    public static int getOpenHeight() {
        return openHeight;
    }

    /**
     * Getter for height of viewpager with toolbar closed
     *
     * @return closed height
     */
    public static int getClosedHeight() {
        return closedHeight;
    }

    /**
     * Setter for height of viewpager with toolbar open
     *
     * @param height open height
     */
    public static void setOpenHeight(int height) {
        openHeight = height;
    }

    /**
     * Setter for height of viewpager with toolbar closed
     *
     * @param height closed height
     */
    public static void setClosedHeight(int height) {
        closedHeight = height;
    }
}

