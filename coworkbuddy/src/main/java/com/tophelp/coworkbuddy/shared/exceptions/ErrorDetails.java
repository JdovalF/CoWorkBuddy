package com.tophelp.coworkbuddy.shared.exceptions;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timestamp, String message, String details) {
}
