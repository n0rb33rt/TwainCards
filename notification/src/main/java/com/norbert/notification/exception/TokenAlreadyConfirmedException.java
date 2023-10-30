package com.norbert.notification.exception;

public class TokenAlreadyConfirmedException extends RuntimeException{
    public TokenAlreadyConfirmedException(String message) {
        super(message);
    }
}
