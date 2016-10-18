package com.bwg.iot;

import org.jboss.resteasy.spi.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // Nothing to do
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

    private ResponseEntity<Object> buildExceptionResponse(HttpStatus status, Exception e) {
        Map response = new HashMap<String, Object>();
        response.put("status", status.value());
        response.put("error", status.name());
        response.put("message", e.getMessage());
        response.put("timestamp", new Date());
        return new ResponseEntity<Object>(response, status);
    }

}
