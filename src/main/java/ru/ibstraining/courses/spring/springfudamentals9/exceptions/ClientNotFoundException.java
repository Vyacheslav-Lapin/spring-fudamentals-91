package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import lombok.RequiredArgsConstructor;

import java.io.Serial;

@SuppressWarnings("java:S1165")
@RequiredArgsConstructor
public class ClientNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  String name;

  @Override
  public String getMessage() {
    return "Client %s not found.".formatted(name);
  }
}
