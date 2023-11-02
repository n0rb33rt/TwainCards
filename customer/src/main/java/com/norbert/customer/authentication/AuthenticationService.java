package com.norbert.customer.authentication;

import com.norbert.customer.authentication.request.SimpleFormAuthenticationRequest;
import com.norbert.customer.authentication.request.SimpleFormRegistrationRequest;
import com.norbert.customer.authentication.response.AuthenticationResponse;
import com.norbert.customer.config.RabbitMQMessageProducer;
import com.norbert.customer.exception.BadRequestException;
import com.norbert.customer.jwt.JwtService;
import com.norbert.customer.user.Role;
import com.norbert.customer.user.User;

import com.norbert.customer.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userJPAService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final static String INTERNAL_EXCHANGE = "internal.exchange";
    private final static String EMAIL_CONFIRMATION_ROUTING_KEY= "internal.email-confirmation.routing-key";
    public AuthenticationResponse authenticate(SimpleFormAuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userJPAService.findUserByEmail(request.email());
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .build();

    }
    public void register(SimpleFormRegistrationRequest request){
        if(userJPAService.isUserPresent(request.email()))
            throw new BadRequestException("Email is already taken");
        User user = buildUser(request);
        userJPAService.save(user);
        sendEmailConfirmationMessage(request);
    }

    private void sendEmailConfirmationMessage(SimpleFormRegistrationRequest request){
        rabbitMQMessageProducer.publish(
                INTERNAL_EXCHANGE,
                EMAIL_CONFIRMATION_ROUTING_KEY,
                request.email()
                );
    }
    private User buildUser(SimpleFormRegistrationRequest request){
        return User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .setPassword(true)
                .role(Role.USER)
                .enabled(false)
                .decks(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .subscriptionUntil(null)
                .build();
    }

}
