package com.team19.gtmovies.exception;

/**
 * Created by matt on 2/11/16.
 * used when attempting to create user with crappy password etc
 */
public class IllegalUserException extends Exception {

    /**
     * default constructor
     */
    public IllegalUserException() {
        super("Invalid arguments when creating new User.");
    }

    /**
     * constructor
     *
     * @param s custom message
     */
    public IllegalUserException(String s) {
        super(s);
    }
}
