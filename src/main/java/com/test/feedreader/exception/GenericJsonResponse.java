package com.test.feedreader.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by sdhruvakumar.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GenericJsonResponse {

    private final int status;
    private final String httpStatus;
    private final String exception;
    private final String message;
    private final String debugMessage;
    private final String requestUrl;

    private GenericJsonResponse(Builder builder) {
        this.status = builder.status;
        this.httpStatus = builder.httpStatus;
        this.exception = builder.exception;
        this.message = builder.message;
        this.debugMessage = builder.debugMessage;
        this.requestUrl = builder.requestUrl;
    }

    public int getStatus() {
        return status;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public static class Builder {
        private int status;
        private String httpStatus;
        private String exception;
        private String message;
        private String debugMessage;
        private String requestUrl;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder httpStatus(String httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder exception(String exception) {
            this.exception = exception;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder debugMessage(String debugMessage) {
            this.debugMessage = debugMessage;
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public GenericJsonResponse build() {
            return new GenericJsonResponse(this);
        }
    }

}