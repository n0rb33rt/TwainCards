package com.norbert.customer.exception;

public class JwtTokenRevokedException extends RuntimeException{
    public JwtTokenRevokedException(String message) {
        super(message);
    }
}
