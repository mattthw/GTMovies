package com.team19.gtmovies.pojo;

import com.team19.gtmovies.exception.IllegalUserException;
import com.team19.gtmovies.exception.NullUserException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by matt on 2/5/16.
 * User is object for storing account info
 */
public class User<T extends Comparable<T>>
        implements Comparable<User<T>>, Serializable {

    private String username;
    private String password;
    private String name;
    private String bio;
    private String major;
    /*
        PERMISSION:
            2: admin
            1: active
            0: locked
           -1: banned

           default: 1/active
     */
    private int permission = 0;
    private String iceCream = "";
    private Map<Integer, Review> myReviews;
    private boolean hasProfile;
    private static final long serialVersionUID = 1L;

    /**
     * constructor used when creating blank user
     */
    public User () {
        username = "null";
        password = "null";
        name = "logged_out";
        bio = "none";
        major = "none";
        hasProfile = false;
        myReviews = new HashMap<Integer, Review>();
    }

    /**
     * USE THIS CONSTRUCTOR WHEN CREATING A USER
     * @param u username
     * @param p password
     * @param n name
     * @throws IllegalUserException if try to create with invalid parameters
     * @throws IllegalArgumentException if parameters null
     */
    public User (String u, String p, String n)
            throws IllegalUserException, IllegalArgumentException {
        if (u == null || p == null || n == null) {
            throw new IllegalArgumentException("Tried creating a user using null parameters.");
        }
        if (u.length() < 4 || p.length() < 4) {
            throw new IllegalUserException("Username and Password must be >= 4 chars");
        }
        username = u;
        password = p;
        name = n;
        bio = "none";
        major = "none";
        permission = 1;
        hasProfile = false;
        myReviews = new HashMap<Integer, Review>();
    }

    /**
     * Add a review to this user's hashmap
     * @param rev the review object to add
     */
    public void addReview(Review rev) {
        myReviews.put(rev.getMovieID(), rev);
//        if(myReviews.containsKey(rev.getMovieID())) {
//            throw new IllegalArgumentException(username + " has already reviewed movieID " +
//                                               rev.getMovieID());
//        } else {
//        }
    }


    /**
     * Remove a review from this user's hashmap
     * @param movieID the movie ID to remove
     */
    public void removeReview(int movieID) {
        if(!myReviews.containsKey(movieID)) {
            throw new IllegalArgumentException(username + " has NOT reviewed movieID " +
                                               movieID);
        } else {
            myReviews.remove(movieID);
        }
    }

    /**
     * Get a movie review from this user's hashmap
     * @param movieID the movie ID of the review to get
     * @return the review corresponding to this movieID
     */
    public Review getReview(int movieID) {
        if(!myReviews.containsKey(movieID)) {
            throw new IllegalArgumentException(username + " has NOT reviewed movieID " +
                    movieID);
        } else {
            return myReviews.get(movieID);
        }
    }

    /**
     * get map of all reviews for user
     * @return hashmap of reviews
     */
    public HashMap<Integer, Review> getReviews() {
        return (HashMap)myReviews;
    }
    /**
     * getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * getter for password
     * @return password
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * getter for identity name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * getter for bio
     * @return bio
     */
    public String getBio() {
        return bio;
    }
    /**
     * getter for major
     * @return major
     */
    public String getMajor() {
        return major;
    }

    /**
     * returns the user's rank tpo callee
     * @return rank
     *
     *  PERMISSION:
     *  2: admin
     *  1: active
     *  0: locked
     * -1: banned
     *
     *  default: 1/active
     */
    public int getPermission() {
        return permission;
    }
    /**
     * does user have a profile
     * @return boolean hasProfile
     */
    public boolean getHasProfile() {
        return hasProfile;
    }

    /**
     * verifies if password correct for class
     * @param tryPassword String password to try
     * @return boolean if password matches
     */
    public boolean correctPassword(String tryPassword) {
        return password.equals(tryPassword);
    }

    /**
     * //TODO: Actually use this lol
     * Used to check a certain achievement as unlocked
     * @param i the unlocked achievement
     */
    public void foundIt(int i) {
        char[] feed = new char[i * i / 2];
        int tmp = 0;
        Random r = new Random();
        int end = Math.max(i * i / 2, iceCream.length());
        int thing = r.nextInt(21);
        if (thing < 7) {
            feed[i * i / 2] = 'k';
        } else if  (thing < 14) {
            feed[i * i / 2] = 'l';
        }
        else {
            feed[i * i / 2] = 'm';
        }
        for (int j = 0; j < end; j++) {
            if (tmp * tmp / 2 == j && iceCream.length() > j) {
                feed[j] = iceCream.charAt(j);
                tmp += 1;
            }
            feed[j] = (char)(r.nextInt(26) + 'a');
        }
        iceCream = new String(feed);
    }
    /**
     * setter for username
     * @param u username
     */
    public void setUsername(String u) {
        username = u;
    }

    /**
     * setter for password
     * @param p password
     */
    public void setPassword(String p) {
        password = p;
    }

    /**
     * setter for name
     * @param n name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * setter for bio
     * @param b bio
     */
    public void setBio(String b) {
        bio = b;
    }

    /**
     * set rank
     * @param p int code for permission
     */
    public void setPermission(int p) {
        permission = p;
    }

    /**
     * setter for boolean hasprofile
     * @param h hasProfile
     */
    public void setHasProfile(boolean h) {
        hasProfile = h;
    }

    /**
     * setter for major
     * @param m major
     */
    public void setMajor(String m) {
        major = m;
    }

    /**
     * compareTo from Comparable
     * @param user other User object
     * @return 0 if equals, else -1
     */
    public int compareTo(User user) {
        if (this.getUsername()
                .equalsIgnoreCase(user.getUsername())) {
            return 0;
        }
        return -1;
    }

    /**
     * equals function
     * @param obj other Obj (User)
     * @return true/false
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        return this.compareTo((User) obj) == 0;
    }

    /**
     * hashcode for User
     * @return hashcode of username
     */
    public int hashCode() {
        return getUsername().hashCode();
    }

    /**
     * tostring
     * @return username, password, and identity name
     */
    public String toString() {
        return ("["
                + this.getUsername()
                + ":" + this.getPassword() + ", "
                + this.getName()
                + ", " + this.getMajor()
        );
    }




}
