package com.kszapsza.allegrointernrecruitment.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<?> handle(RuntimeException ex, WebRequest request, HttpStatus httpStatus, String messageText) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ExceptionErrorMessage message = new ExceptionErrorMessage(httpStatus, messageText);
        return handleExceptionInternal(ex, message, httpHeaders, httpStatus, request);
    }

    @ExceptionHandler({TimeoutException.class})
    protected ResponseEntity<?> handleUpstreamTimeoutException(TimeoutException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.GATEWAY_TIMEOUT, "Request timed out.");
    }

    @ExceptionHandler({HttpStatusCodeException.class})
    protected ResponseEntity<?> handleUpstreamHttpStatusCodeException(HttpStatusCodeException ex, WebRequest request) {
        return handle(ex, request, ex.getStatusCode(), null);
    }
}
