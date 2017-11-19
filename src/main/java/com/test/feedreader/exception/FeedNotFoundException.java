package com.test.feedreader.exception;

/**
 * Created by sdhruvakumar.
 */
public class FeedNotFoundException extends RuntimeException {

    private final long id;

    public FeedNotFoundException(final long id) {
        super("Feed could not be found with id: " + id);
        this.id = id;
    }

}