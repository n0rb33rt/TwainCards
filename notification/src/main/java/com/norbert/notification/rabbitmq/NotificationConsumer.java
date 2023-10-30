package com.norbert.notification.rabbitmq;

import com.norbert.notification.email_confirmation_token.EmailConfirmationTokenService;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationConsumer {
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    @RabbitListener(
            queues = {"email-confirmation.queue"},
            messageConverter = "jacksonConverter",
            ackMode = "NONE"
    )
    public void emailConfirmationConsumer(String email){
        emailConfirmationTokenService.sendConfirmationMail(email);
    }


}
