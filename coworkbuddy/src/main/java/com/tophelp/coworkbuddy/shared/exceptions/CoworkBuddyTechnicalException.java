package com.tophelp.coworkbuddy.shared.exceptions;

public class CoworkBuddyTechnicalException extends RuntimeException {

    public CoworkBuddyTechnicalException() {
        super();
    }
    public CoworkBuddyTechnicalException(String message) {
        super(message);
    }
    public CoworkBuddyTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoworkBuddyTechnicalException(Throwable cause) {
        super(cause);
    }
}
