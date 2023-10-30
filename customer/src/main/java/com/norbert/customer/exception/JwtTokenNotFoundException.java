package com.norbert.customer.exception;

public class JwtTokenNotFoundException extends RuntimeException{
    public JwtTokenNotFoundException(String message) {
        super(message);
    }
}
