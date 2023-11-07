package com.tophelp.coworkbuddy.shared.exceptions;

public class CoworkBuddyTechnicalException extends RuntimeException {

  public CoworkBuddyTechnicalException(String message) {
    super(message);
  }

  public CoworkBuddyTechnicalException(Throwable cause) {
    super(cause);
  }
}
