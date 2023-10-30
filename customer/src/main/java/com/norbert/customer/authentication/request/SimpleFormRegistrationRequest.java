package com.norbert.customer.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;

import org.hibernate.validator.constraints.Length;


@Builder
public record SimpleFormRegistrationRequest(
        @JsonProperty("email")
        @Email(message = "Email is not valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        String email,
        @JsonProperty("password")
        @Length(min = 6, message = "Password must be over or equal 6 symbols")
        @NotBlank(message = "Password must be not empty")
        String password
) {
}
