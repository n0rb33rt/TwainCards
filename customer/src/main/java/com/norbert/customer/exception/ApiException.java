package com.norbert.customer.exception;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ApiException(
        String error,
        String message,
        String path
) {
}
