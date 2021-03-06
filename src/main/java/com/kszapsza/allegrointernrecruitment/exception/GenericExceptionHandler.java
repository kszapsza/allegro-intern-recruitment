package com.kszapsza.allegrointernrecruitment.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<?> handle(RuntimeException ex, WebRequest request, HttpStatus httpStatus, String messageText) {
        ExceptionErrorMessage message = new ExceptionErrorMessage(httpStatus, messageText);
        return handleExceptionInternal(ex, message, new HttpHeaders(), httpStatus, request);
    }

    @ResponseBody
    @ExceptionHandler({TimeoutException.class})
    protected ResponseEntity<?> handleUpstreamTimeoutException(TimeoutException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.GATEWAY_TIMEOUT, "Request timed out.");
    }

    @ResponseBody
    @ExceptionHandler({GithubHttpException.class})
    protected ResponseEntity<?> handleUpstreamHttpStatusCodeException(GithubHttpException ex, WebRequest request) {
        try {
            String message = new ObjectMapper()
                    .readValue(ex.getResponseBodyJson(), GithubErrorMessage.class)
                    .getMessage();
            return handle(ex, request, ex.getStatusCode(), message);
        } catch (JsonProcessingException e) {
            return handle(ex, request, ex.getStatusCode(), null);
        }

    }
}
