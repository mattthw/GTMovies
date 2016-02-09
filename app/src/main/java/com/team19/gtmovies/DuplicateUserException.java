package com.team19.gtmovies;

/**
 * Created by matt on 2/8/16.
 * used when account already exists
 */
public class DuplicateUserException extends Exception {

    public DuplicateUserException () {
        super("An account with this username already exists!");
    }
    public DuplicateUserException(String message) {
        super(message);

    }
}
