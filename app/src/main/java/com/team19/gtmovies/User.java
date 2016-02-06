package com.team19.gtmovies;

/**
 * Created by matt on 2/5/16.
 * User is object for storing acocunt info
 */
public class User {
    private String username;
    private String password;//TODO: yeah maybe plaintext is bad.....
    private String name;

    public User (String u, String p, String n) {
        username = u;
        password = p;
        name = n;
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
    public void setCredentials(String cred) {
        String u = cred.substring(0, cred.indexOf(":"));
        String p = cred.substring(cred.indexOf(":") + 1);
        username = u;
        password = p;
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
}
