package com.epam.esm.exceptionhandler.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
