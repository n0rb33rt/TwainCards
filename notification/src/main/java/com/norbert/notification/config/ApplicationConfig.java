package com.norbert.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class ApplicationConfig {
    @Value("${rabbitmq.exchanges.internal}")
    private String INTERNAL_EXCHANGE;
    private final static String EMAIL_CONFIRMATION_ROUTING_KEY= "internal.email-confirmation.routing-key";
    private final static String EMAIL_CONFIRMATION_QUEUE= "email-confirmation.queue";
    @Bean
    public Queue emailConfirmationQueue() {
        return new Queue(EMAIL_CONFIRMATION_QUEUE);
    }
    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.INTERNAL_EXCHANGE);
    }



    @Bean
    public Binding emailConfirmationBinding() {
        return BindingBuilder
                .bind(emailConfirmationQueue())
                .to(internalTopicExchange())
                .with(EMAIL_CONFIRMATION_ROUTING_KEY);
    }


    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    @Bean
    public MessageConverter jacksonConverter(){
        return new SimpleMessageConverter();
    }

}
