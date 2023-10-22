package com.tophelp.coworkbuddy.infrastructure.exceptions;

public class DatabaseNotFoundException extends RuntimeException {
    public DatabaseNotFoundException(String message) {
        super(message);
    }
}
