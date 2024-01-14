package com.norbert.translator.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final HttpServletRequest request;
    @ExceptionHandler(value = {RequestSendingException.class})
    public ResponseEntity<Object> handleBadRequestException(RuntimeException exception){
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
