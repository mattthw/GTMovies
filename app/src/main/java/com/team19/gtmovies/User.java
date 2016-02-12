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
    private static final long serialVersionUID = 1L;

    /**
     * constructor used when creating blank user
     */
    public User () {
        username = "null";
        password = "null";
        name = "logged_out";
    }
    public User (String u, String p, String n)
            throws IllegalUserException {
        username = u;
        password = p;
        name = n;
        if (u.length() < 4 || p.length() < 4) {
            throw new IllegalUserException("Username and Password must be >= 4 chars");
        }
    }

    public String getCredentials() {
        return username + ":" + password;
    }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
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
    public void setCredentials(String cred) {
        String u = cred.substring(0, cred.indexOf(":"));
        String p = cred.substring(cred.indexOf(":") + 1);
        username = u;
        password = p;
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
