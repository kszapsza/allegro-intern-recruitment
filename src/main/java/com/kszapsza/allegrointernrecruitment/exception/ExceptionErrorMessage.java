package com.kszapsza.allegrointernrecruitment.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ExceptionErrorMessage {
    private final String status;
    private final Date timestamp;
    private final String message;

    public ExceptionErrorMessage(HttpStatus status, String message) {
        this.status = status.getReasonPhrase();
        this.timestamp = new Date();
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}