package com.team19.gtmovies;

/**
 * Created by matt on 2/11/16.
 * used when attempting to create user with crappy password etc
 */
public class IllegalUserException extends Exception {

    public IllegalUserException() {
        super("Invalid arguments when creating new User.");
    }
    public IllegalUserException(String s) {
        super(s);
    }
}
