package com.revlearn.team1.exceptions;

public class ServiceLayerDataAccessException extends RuntimeException {
    public ServiceLayerDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
