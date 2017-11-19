package com.test.feedreader;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * Created by sdhruvakumar.
 */
public class FeedReaderConstants {

    public static final String UTF8 = "UTF8";
    public static final String USER_CONTROLLER_ENDPOINT = "/users";
    public static final String FEED_CONTROLLER_ENDPOINT = "/feeds";
    public static final String ARTICLE_CONTROLLER_ENDPOINT = "/articles";

    public static final String JSON_CONTENT_TYPE = "application/hal+json;charset=UTF-8";

    private FeedReaderConstants() {

    }
}
