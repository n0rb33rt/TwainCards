package com.norbert.customer.exception;

public class DeckNotFoundException extends RuntimeException{
    public DeckNotFoundException(String message) {
        super(message);
    }
}
