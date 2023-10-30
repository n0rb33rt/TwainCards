package com.norbert.notification.exception;

public class MailSendingException extends RuntimeException{
    public MailSendingException(String message) {
        super(message);
    }
}
