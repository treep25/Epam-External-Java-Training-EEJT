package com.epam.esm;

import org.springframework.http.HttpStatus;

public class ResponseStatus {

    private final HttpStatus status;

    public ResponseStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getResponse() {
        return status;
    }
}
