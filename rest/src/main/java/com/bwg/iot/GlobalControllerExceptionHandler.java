package com.bwg.iot;

import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;
import java.util.*;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);


    @ResponseStatus(HttpStatus.ALREADY_REPORTED)  // 208
    @ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<Object> handleAlready(Exception e) {
        return buildExceptionResponse(HttpStatus.ALREADY_REPORTED, e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConflict(Exception e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNotFound(Exception e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(Exception e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)  // 405
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(Exception e) {
        return buildExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(Exception e) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(Exception e) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<Object> buildExceptionResponse(HttpStatus status, Exception e) {
        Map response = new HashMap<String, Object>();
        response.put("status", status.value());
        response.put("error", status.name());
        response.put("message", e.getMessage());
        response.put("timestamp", new Date());

        LOGGER.error(e.getClass().getName() + " : " + e.getMessage());
        LOGGER.error("return code:" + status.value() + " : " + status.name());
        return new ResponseEntity<Object>(response, status);
    }

}
