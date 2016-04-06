package com.team19.gtmovies.exception;

/**
 * Created by matt on 2/8/16.
 * used when account already exists
 */
public class DuplicateUserException extends Exception {

    /**
     * default constructor
     */
    public DuplicateUserException() {
        super("An account with this username already exists!");
    }

    /**
     * constructor
     *
     * @param message custom message
     */
    public DuplicateUserException(String message) {
        super(message);

    }
}
