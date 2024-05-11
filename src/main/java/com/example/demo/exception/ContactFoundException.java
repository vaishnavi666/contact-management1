package com.example.demo.exception;

public class ContactFoundException extends RuntimeException {
    public ContactFoundException(String message) {

        super("ContactFoundException : " +message);
    }
}
