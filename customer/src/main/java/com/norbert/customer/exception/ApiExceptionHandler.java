package com.norbert.customer.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final HttpServletRequest request;
    @ExceptionHandler(value = {BadRequestException.class, BadCredentialsException.class, DisabledException.class})
    public ResponseEntity<Object> handleBadRequestException(RuntimeException exception){
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

}
