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
    public User (String u, String p, String n)
            throws IllegalUserException, NullUserException {
        username = u;
        password = p;
        name = n;
        bio = "";
        hasProfile = false;
        if (u.length() < 4 || p.length() < 4) {
            throw new IllegalUserException("Username and Password must be >= 4 chars");
        }
        if (u == null || p == null || n == null) {
            throw new NullUserException("Tried creating a user using null parameters.");
        }
    }

    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getBio() {
        return bio;
    }
    public String getMajor() {
        return major;
    }
    public boolean getHasProfile() {
        return hasProfile;
    }
    protected String getPassword() {
        return this.password;
    }
    public void setUsername(String u) {
        username = u;
    }
    public void setPassword(String p) {
        password = p;
    }
    public void setName(String n) {
        name = n;
    }
    public void setBio(String b) {
        bio = b;
    }
    public void setHasProfile(boolean h) {
        hasProfile = h;
    }
    public void setMajor(String m) {
        major = m;
    }
    public int compareTo(User user) {
        if (this.getUsername()
                .equalsIgnoreCase(user.getUsername())) {
            return 0;
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        if (this.compareTo((User)obj) == 0) {
            return true;
        }
        return false;
    }
    public int hashCode() {
        return getUsername().hashCode();
    }
    public String toString() {
        return this.getUsername()
                + ":" + this.getPassword()
                + " " + this.getName();
    }


}
