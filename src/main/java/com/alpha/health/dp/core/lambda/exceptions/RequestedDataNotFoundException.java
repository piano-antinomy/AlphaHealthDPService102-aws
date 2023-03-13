package com.alpha.health.dp.core.lambda.exceptions;

public class RequestedDataNotFoundException extends RuntimeException {
    public RequestedDataNotFoundException(Throwable t) {
        super(t);
    }

    public RequestedDataNotFoundException(Exception e) {
        super(e);
    }

    public RequestedDataNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
