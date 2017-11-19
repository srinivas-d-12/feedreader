package com.test.feedreader.exception;

/**
 * Created by sdhruvakumar.
 */
public class UserNotFoundException extends RuntimeException {

    private final long id;

    public UserNotFoundException(final long id) {
        super("User could not be found with id: " + id);
        this.id = id;
    }

}