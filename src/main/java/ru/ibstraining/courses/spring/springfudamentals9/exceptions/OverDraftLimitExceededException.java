package ru.ibstraining.courses.spring.springfudamentals9.exceptions;

import java.io.Serial;

public class OverDraftLimitExceededException extends NotEnoughFundsException {

    @Serial private static final long serialVersionUID = 1L;
    String account;

    public OverDraftLimitExceededException(String account, double amount) {
        super(amount);
        this.account = account;
    }

    @Override
    public String getMessage() {
        return "Overdraft Limit exceeded on %s amount: %s".formatted(account, amount);

    }
}
