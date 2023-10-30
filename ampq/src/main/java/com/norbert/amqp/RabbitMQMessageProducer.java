package com.norbert.amqp;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RabbitMQMessageProducer {
    private final AmqpTemplate amqpTemplate;
    public void publish(String exchange, String routingKey, Object payload){
        amqpTemplate.convertAndSend(exchange,routingKey,payload);
    }
}
