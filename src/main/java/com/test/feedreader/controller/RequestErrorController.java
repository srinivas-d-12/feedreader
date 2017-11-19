package com.test.feedreader.controller;

import com.test.feedreader.exception.RequestNotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdhruvakumar.
 */
@RestController
public class RequestErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public void error() throws RequestNotFoundException {
        throw new RequestNotFoundException("The requested url has no mapping");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
