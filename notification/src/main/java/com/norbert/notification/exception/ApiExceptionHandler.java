package com.norbert.notification.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(value = {MailSendingException.class})
    public ResponseEntity<Object> handleBadRequest(MailSendingException exception) {
        ApiException apiException = new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {TokenExpiredException.class, InvalidTokenException.class,TokenAlreadyConfirmedException.class,RuntimeException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException exception) {
        ApiException apiException = new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


}
