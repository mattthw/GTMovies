package com.team19.gtmovies;

/**
 * Created by matt on 2/8/16.
 * used if user does not exist
 */
public class NullUserException extends Exception {
    public NullUserException() {
        super("User not found");
    }
    public NullUserException(String message) {
        super(message);
    }
}
