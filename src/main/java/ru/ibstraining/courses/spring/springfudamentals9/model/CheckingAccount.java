package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.OverDraftLimitExceededException;

import java.util.Objects;

import static lombok.AccessLevel.*;
import static ru.ibstraining.courses.spring.springfudamentals9.commons.HibernateUtils.*;

@SuppressWarnings({"com.haulmont.ampjpb.LombokDataInspection",
                   "com.intellij.jpb.LombokDataInspection"})

@Data
@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(access = PRIVATE)
public final class CheckingAccount extends Account {

  @SuppressWarnings("NullableProblems")
  @NonNull double overdraft;

  double balance;

  public static CheckingAccount CheckingAccount() {
    return CheckingAccount(0);
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

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof CheckingAccount checkingAccount
                        && getEffectiveClass(this) == getEffectiveClass(checkingAccount)
                        && getId() != null
                        && Objects.equals(getId(), checkingAccount.getId());
  }

  @Override
  public int hashCode() {
    return getEffectiveClass(this).hashCode();
  }
}
