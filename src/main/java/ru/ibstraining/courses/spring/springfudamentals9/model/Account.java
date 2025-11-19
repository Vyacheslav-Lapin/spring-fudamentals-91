package ru.ibstraining.courses.spring.springfudamentals9.model;

import ru.ibstraining.courses.spring.springfudamentals9.exceptions.OverDraftLimitExceededException;

import java.util.UUID;

public sealed interface Account permits CheckingAccount, SavingAccount {

  UUID getId();

  double getBalance();
  Account setBalance(double balance);

  default void deposit(double amount) {
    if (amount < 0) return;
    setBalance(amount);
  }

  void withdraw(double amount) throws OverDraftLimitExceededException;
}
