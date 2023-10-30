package com.norbert.customer.authentication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;



@Builder
public record AuthenticationResponse(
        @JsonProperty("access_token") String accessToken
) {
}
