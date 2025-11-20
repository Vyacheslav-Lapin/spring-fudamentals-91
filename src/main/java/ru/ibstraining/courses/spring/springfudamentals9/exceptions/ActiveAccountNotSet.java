package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import lombok.RequiredArgsConstructor;

import java.io.Serial;

@RequiredArgsConstructor
@SuppressWarnings("java:S1165")
public class ActiveAccountNotSet extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;
  String clientName;

  @Override
  public String getMessage() {
    return "Active account not set for " + clientName;
  }
}
