package com.tophelp.coworkbuddy.application.utils;

import com.tophelp.coworkbuddy.shared.exceptions.CoworkBuddyTechnicalException;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static java.util.Objects.isNull;

@UtilityClass
public class CrudUtils {

  public void throwExceptionWhenNull(String parameter, String parameterName, boolean needsToBeNotNull) {
    if (isNull(parameter) == needsToBeNotNull) {
      throw new CoworkBuddyTechnicalException(String.format("%s %s required", parameterName,
          needsToBeNotNull ? "is" : "is not"));
    }
  }

  public UUID uuidFromString(String uuid) {
    try {
      return UUID.fromString(uuid);
    } catch (IllegalArgumentException exception) {
      throw new CoworkBuddyTechnicalException(String.format("Id: %s is not a valid UUID", uuid));
    }
  }

}
