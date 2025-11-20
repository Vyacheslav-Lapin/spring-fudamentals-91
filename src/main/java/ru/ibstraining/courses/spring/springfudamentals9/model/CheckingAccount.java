package ru.ibstraining.courses.spring.springfudamentals9.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.NonFinal;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.OverDraftLimitExceededException;

import java.util.UUID;

import static lombok.AccessLevel.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = PRIVATE)
public final class CheckingAccount implements Account {

  UUID id =  UUID.randomUUID();

  @SuppressWarnings("NullableProblems")
  @NonFinal @NonNull double overdraft;

  @NonFinal double balance;

  public static CheckingAccount CheckingAccount() {
    return new CheckingAccount(0);
  }

  @SuppressWarnings({"java:S112", "MethodNameSameAsClassName"})
  public static CheckingAccount CheckingAccount(double overdraft) {
    if (overdraft < 0) {
      throw new RuntimeException("Овердрафт должен быть больше нуля!");
    }

    return new CheckingAccount(overdraft);
  }

  public void setOverdraft(CheckingAccount this, double amount) {
    if (overdraft < 0) return;
    overdraft = amount;
  }

  @Override
  public void withdraw(double amount) {

    if (balance + overdraft < amount) {
      throw new OverDraftLimitExceededException(
          getClass().getSimpleName(), balance + overdraft);
    }

    setBalance(balance - amount);
  }

  public double getBalance() {
    return balance - overdraft;
  }
}
