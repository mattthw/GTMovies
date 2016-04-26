package com.team19.gtmovies.data;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.team19.gtmovies.pojo.User;

/**
 * Created by Austin on 2/22/2016.
 * used by all other classes when requesting current user info
 * rgtrgrgtrs
 */
public class CurrentState {
    //    private static final CurrentState ourInstance = new CurrentState();
    private static User currentUser;
    private static DisplayMetrics displayMetrics;
    private static int openHeight = 0;
    private static int closedHeight = 0;

//    public static CurrentState getInstance() {
//        return ourInstance;
//    }

    /**
     * default constructor for CurrentState
     *
     * @param u Require the correct user as parameter as form of
     *          DEPENDENCY INJECTION. Replaces singleton implementation.
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
     * Sets display metrics to use for heights.
     *
     * @param mMetrics current application displaymetrics
     */
    public static void setDisplayMetrics(DisplayMetrics mMetrics) {
        displayMetrics = mMetrics;
    }

    /**
     * Sets heights of MainActivity's movie lists with toolbar opened and closed.
     *
     * @param pxToolbarHeight height of toolbar in px
     */
    public static void setHeights(int pxToolbarHeight) {
        if (displayMetrics == null || pxToolbarHeight <= 0) {
            return;
        }
        int dpHeight = convertPxtoDp(displayMetrics.heightPixels);
        int dpToolbarHeight = convertPxtoDp(pxToolbarHeight);
        setOpenHeight(dpHeight, dpToolbarHeight);
        setClosedHeight(dpHeight);
    }

    /**
     * Setter for height of viewpager with toolbar open
     *
     * @param height open height
     * @param toolbarHeight height of toolbar in dp
     */
    private static void setOpenHeight(int height, int toolbarHeight) {
        openHeight = height - toolbarHeight;
    }

    /**
     * Setter for height of viewpager with toolbar closed
     *
     * @param height closed height
     */
    private static void setClosedHeight(int height) {
        closedHeight = height;
    }

    /**
     * Pixel to display independent pixel (dp) converter.
     *
     * @param pixels int of pixels to convert
     * @return int of dp conversion
     */
    private static int convertPxtoDp(int pixels) {
        return (int) (pixels / ((float) displayMetrics.density / DisplayMetrics.DENSITY_DEFAULT));
    }
}

