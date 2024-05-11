package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ContactBookExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ContactNotFoundException.class)
    public final ResponseEntity handleContactNotFoundException(final ContactNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @ExceptionHandler(ContactFoundException.class)
    public final ResponseEntity handleContactAlreadyExistsException(final ContactFoundException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), HttpStatus.FOUND);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
