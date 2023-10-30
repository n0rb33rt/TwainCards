package com.norbert.notification.util;

public interface EmailSender {
    void send(SendingEmailRequest request);
}