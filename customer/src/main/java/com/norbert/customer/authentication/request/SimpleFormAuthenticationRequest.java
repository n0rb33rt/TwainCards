package com.norbert.customer.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
public record SimpleFormAuthenticationRequest(
        @JsonProperty("email")
        @NotBlank(message = "Email must be not empty")
        String email,
        @JsonProperty("password")
        @NotBlank(message = "Password must be not empty")
        String password
) {
}
