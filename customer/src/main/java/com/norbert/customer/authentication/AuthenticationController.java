package com.norbert.customer.authentication;


import com.norbert.customer.authentication.request.SimpleFormAuthenticationRequest;
import com.norbert.customer.authentication.request.SimpleFormRegistrationRequest;
import com.norbert.customer.authentication.response.AuthenticationResponse;
import com.norbert.customer.authentication.request.GoogleAuthenticationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final GoogleAuthenticationService googleAuthenticationService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid SimpleFormAuthenticationRequest authenticationRequest
    ){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid SimpleFormRegistrationRequest authenticationRequest
    ){
        authenticationService.register(authenticationRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping ("/google/login")
    public ResponseEntity<AuthenticationResponse> authenticateByGoogle(
            @RequestBody GoogleAuthenticationRequest request
    ) {
        return ResponseEntity.ok(googleAuthenticationService.authenticate(request));
    }
}
