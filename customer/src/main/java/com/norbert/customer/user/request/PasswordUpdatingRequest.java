package com.norbert.customer.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PasswordUpdatingRequest(
        @JsonProperty("password")
        @Length(min = 6, message = "Password must be over or equal 6 symbols")
        @NotBlank(message = "Password must be over or equal 6 symbols")
        String password
) {
}
