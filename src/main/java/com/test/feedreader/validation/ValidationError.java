package com.test.feedreader.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sdhruvakumar.
 */
public class ValidationError {

    private final String errorCount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<String>> errorMessages = new HashMap<>();

    public ValidationError(String errorCount) {
        this.errorCount = errorCount;
    }

    public Map<String, List<String>> getErrorMessages() {
        return errorMessages;
    }

    public void addValidationErrorMessage(String errorField, String errorMessage) {
        List<String> fieldErrorMessages = this.errorMessages.get(errorField);
        if (fieldErrorMessages == null) {
            fieldErrorMessages = new ArrayList<>();
            this.errorMessages.put(errorField, fieldErrorMessages);
        }
        fieldErrorMessages.add(errorMessage);
    }

    public String getErrorCount() {
        return errorCount;
    }
}
