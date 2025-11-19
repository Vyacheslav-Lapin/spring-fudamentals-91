package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import lombok.RequiredArgsConstructor;

import java.io.Serial;

@RequiredArgsConstructor
public class NotEnoughFundsException extends BankException {

    @Serial private static final long serialVersionUID = 1L;

    protected double amount;

    @Override
    public String getMessage() {
        return "Not Enough Funds " + amount;
    }
}
