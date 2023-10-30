package com.norbert.notification.email_confirmation_token;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/confirm")
public class EmailConfirmationTokenController {
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @GetMapping
    public String confirmAccount(
            @RequestParam("token") String token
    ){
        emailConfirmationTokenService.confirmAccount(token);
        return "You have successfully confirmed your account. Thanks for registration";
    }

}
