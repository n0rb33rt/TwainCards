package com.norbert.notification.util;

public record SendingEmailRequest(
        String message, String email,String subject
) {
}
