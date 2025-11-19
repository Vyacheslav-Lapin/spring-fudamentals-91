package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("java:S1165")
public class ActiveAccountNotSet extends RuntimeException {
    String clientName;

    @Override
    public String getMessage() {
        return "Active account not set for " + clientName;
    }
}
