package com.norbert.notification.email_confirmation_token;


import com.norbert.notification.exception.InvalidTokenException;
import com.norbert.notification.exception.TokenAlreadyConfirmedException;
import com.norbert.notification.exception.TokenExpiredException;
import com.norbert.notification.jwt.JwtService;
import com.norbert.notification.util.BuildingEmailMessageRequest;
import com.norbert.notification.util.EmailBuilder;
import com.norbert.notification.util.EmailSender;
import com.norbert.notification.util.SendingEmailRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailConfirmationTokenService {
    private final EmailConfirmationTokenDAO emailConfirmationTokenDAO;
    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final static String BASE_CONFIRMATION_URL = "http://localhost:8081/api/v1/confirm?token=";
    public void sendConfirmationMail(String email){
        String message = buildSendingMessage(email);
        SendingEmailRequest sendingEmailRequest = new SendingEmailRequest(message, email, "Confirm your account");
        emailSender.send(sendingEmailRequest);
    }
    private String buildSendingMessage(String email){
        EmailConfirmationToken token = buildEmailConfirmationToken(email);
        String name = buildNameFromEmail(email);
        String link = buildConfirmationLink(token);
        emailConfirmationTokenDAO.save(token);
        BuildingEmailMessageRequest buildingEmailMessageRequest = new BuildingEmailMessageRequest(name,link);
        return EmailBuilder.buildEmailConfirmationMessage(buildingEmailMessageRequest);
    }

    private EmailConfirmationToken buildEmailConfirmationToken(String email){
        return EmailConfirmationToken.builder()
                .token(jwtService.generateToken(email))
                .confirmed(false)
                .build();
    }
    private String buildConfirmationLink(EmailConfirmationToken token){
        return BASE_CONFIRMATION_URL + token.getToken();
    }
    private String buildNameFromEmail(String email){
        return email.substring(0,email.indexOf("@"));
    }

    public void confirmAccount(String token){
        EmailConfirmationToken emailConfirmationToken =
                emailConfirmationTokenDAO.findByToken(token).orElseThrow(()-> new InvalidTokenException("Confirmation link is not valid"));
        if(emailConfirmationToken.getConfirmed())
            throw new TokenAlreadyConfirmedException("Token is already confirmed");
        if(jwtService.isTokenExpired(token))
            throw new TokenExpiredException("Confirmation link is expired, try to register again");
        emailConfirmationTokenDAO.enableAccount(jwtService.extractEmail(token));
        emailConfirmationToken.setConfirmed(true);
        emailConfirmationTokenDAO.update(emailConfirmationToken);
    }

}
