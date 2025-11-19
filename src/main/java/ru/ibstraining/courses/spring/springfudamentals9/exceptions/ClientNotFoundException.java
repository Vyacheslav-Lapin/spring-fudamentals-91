package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("java:S1165")
@RequiredArgsConstructor
public class ClientNotFoundException extends RuntimeException {

  String name;

  @Override
  public String getMessage() {
    return "Client " + name + " not found.";
  }
}
