package com.kszapsza.allegrointernrecruitment.exception;

import org.springframework.http.HttpStatus;

public class GithubHttpException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String responseBodyJson;

    public GithubHttpException(String responseBodyJson, HttpStatus statusCode) {
        this.responseBodyJson = responseBodyJson;
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getResponseBodyJson() {
        return responseBodyJson;
    }
}
