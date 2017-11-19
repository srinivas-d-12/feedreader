package com.test.feedreader.controller.support;

import com.test.feedreader.exception.GenericJsonResponse;
import com.test.feedreader.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by sdhruvakumar.
 */
@ControllerAdvice
public class FeedControllerAdvice {


    @Autowired
    private MessageSource msgSource;

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleValidationException(MethodArgumentNotValidException methodArgNotValidException) {
        BindingResult result = methodArgNotValidException.getBindingResult();
        return resolveFieldErrors(result);
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleBindException(BindException bindException) {
        BindingResult result = bindException.getBindingResult();
        return resolveFieldErrors(result);
    }

    public ValidationError resolveFieldErrors(BindingResult result) {
        ValidationError error = new ValidationError("Validation failed with " + result.getErrorCount() + " error(s)");
        for (FieldError fieldError : result.getFieldErrors()) {
            error.addValidationErrorMessage(fieldError.getField(), getErrorMessage(fieldError));
        }
        return error;
    }

    private String getErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String errorMessage = "";
        try {
            errorMessage = msgSource.getMessage(fieldError.getCode(), null, currentLocale);
        } catch (Exception ex) {
            errorMessage = fieldError.getDefaultMessage() == null ? fieldError.getCodes()[0]
                    : String.format("%s - %s.%s", fieldError.getDefaultMessage(), fieldError.getObjectName(),
                    fieldError.getField());
        }
        return errorMessage;
    }


    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericJsonResponse entityNotFoundErrorHandler(HttpServletRequest request, HttpServletResponse response,
                                                          EntityNotFoundException exception) {
        return new GenericJsonResponse.Builder().requestUrl(request.getRequestURI())
                .status(HttpStatus.NOT_FOUND.value()).httpStatus(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage()).build();
    }


    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GenericJsonResponse dataConflictErrorHandler(HttpServletRequest request,
                                                        DataIntegrityViolationException exception) {
        return new GenericJsonResponse.Builder().requestUrl(request.getRequestURI()).status(HttpStatus.CONFLICT.value())
                .httpStatus(HttpStatus.CONFLICT.name()).message("Data conflict with the given request")
                .debugMessage(exception.getCause().getCause().getMessage()).build();
    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericJsonResponse defaultErrorHandler(HttpServletRequest request, Exception exception) throws Exception { // NOSONAR
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) throw exception;
        return new GenericJsonResponse.Builder().requestUrl(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value()).httpStatus(HttpStatus.BAD_REQUEST.name())
                .message("The request could not be understood by the server. The client SHOULD NOT repeat the request without modifications.").debugMessage(exception.getMessage()).build();
    }


}
