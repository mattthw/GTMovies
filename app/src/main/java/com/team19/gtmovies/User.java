package com.team19.gtmovies;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by matt on 2/5/16.
 * User is object for storing acocunt info
 */
public class User<T extends Comparable<T>>
        implements Comparable<User<T>>, Serializable {

    private String username;
    private String password;
    private String name;
    private String bio;
    private String major;
    private boolean hasProfile;
    private static final long serialVersionUID = 1L;

    /**
     * constructor used when creating blank user
     */
    public User () {
        username = "null";
        password = "null";
        name = "logged_out";
        bio = "";
        major = "";
        hasProfile = false;
    }

    /**
     * USE THIS CONSTRUCTOR WHEN CREATING A USER
     * @param u username
     * @param p password
     * @param n name
     * @throws IllegalUserException if try to create with invalid parameters
     * @throws NullUserException if parameters null
     */
    public User (String u, String p, String n)
            throws IllegalUserException, NullPointerException {
        username = u;
        password = p;
        name = n;
        bio = "";
        hasProfile = false;
        if (u.length() < 4 || p.length() < 4) {
            throw new IllegalUserException("Username and Password must be >= 4 chars");
        }
        if (u == null || p == null || n == null) {
            throw new NullPointerException("Tried creating a user using null parameters.");
        }
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
    protected String getPassword() {
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
     * does user have a profile
     * @return boolean hasProfile
     */
    public boolean getHasProfile() {
        return hasProfile;
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
        if (this.compareTo((User)obj) == 0) {
            return true;
        }
        return false;
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
        return this.getUsername()
                + ":" + this.getPassword()
                + " " + this.getName();
    }


}
