package com.norbert.customer.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleAuthenticationRequest
        (       @JsonProperty("access_token")
                String accessToken
        ) {
}
