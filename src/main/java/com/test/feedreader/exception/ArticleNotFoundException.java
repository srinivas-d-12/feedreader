package com.test.feedreader.exception;

/**
 * Created by sdhruvakumar.
 */
public class ArticleNotFoundException extends RuntimeException {

    private final long id;

    public ArticleNotFoundException(final long id) {
        super("Article could not be found with id: " + id);
        this.id = id;
    }

}