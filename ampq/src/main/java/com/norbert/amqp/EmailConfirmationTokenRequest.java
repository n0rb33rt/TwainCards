package com.norbert.amqp;



public record
EmailConfirmationTokenRequest
        (
                String email
        ) {
}
