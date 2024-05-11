package com.example.demo.exception;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(String message) {

        super("ContactNotFoundException : " +message);
    }
}
