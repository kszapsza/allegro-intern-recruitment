package com.kszapsza.allegrointernrecruitment.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ExceptionErrorMessage {
    private final String status;
    private final LocalDateTime timestamp;
    private final String message;

    @JsonCreator
    public ExceptionErrorMessage(@JsonProperty String status, @JsonProperty String timestamp, @JsonProperty String message) {
        this.status = status;
        this.timestamp = LocalDateTime.parse(timestamp);
        this.message = message;
    }

    public ExceptionErrorMessage(HttpStatus status, String message) {
        this.status = status.getReasonPhrase();
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}