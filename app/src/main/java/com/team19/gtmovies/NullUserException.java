package com.team19.gtmovies;

/**
 * Created by matt on 2/8/16.
 * used if user does not exist
 */
public class NullUserException extends Exception {
    /**
     * default constructor
     */
    public NullUserException() {
        super("User not found");
    }

    /**
     * constructor
     * @param message custom message
     */
    public NullUserException(String message) {
        super(message);
    }
}
