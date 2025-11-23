package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("CHECKING")
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(access = PRIVATE)
public class CheckingAccount extends Account<CheckingAccount> {

  @SuppressWarnings("NullableProblems")
  @NonNull double overdraft;

  @SuppressWarnings({"java:S112", "java:S100", "java:S1223", "MethodNameSameAsClassName"})
  public static CheckingAccount CheckingAccount(double overdraft) {
    if (overdraft < 0)
      throw new RuntimeException("Овердрафт должен быть больше нуля!");

    return new CheckingAccount(overdraft);
  }

  @SuppressWarnings("java:S112")
  public void setOverdraft(double amount) {
    if (overdraft < 0)
      throw new RuntimeException("Овердрафт должен быть больше нуля!");

    overdraft = amount;
  }

  @Override
  public void withdraw(double amount) {

    if (balance + overdraft < amount)
      throw new OverDraftLimitExceededException(
          getClass().getSimpleName(),
          balance + overdraft);

    setBalance(balance - amount);
  }

  @Override
  public double getBalance() {
    return balance - overdraft;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof CheckingAccount checkingAccount
                        && getEffectiveClass(this) == getEffectiveClass(checkingAccount)
                        && id != null
                        && Objects.equals(id, checkingAccount.id);
  }

  @Override
  public int hashCode() {
    return getEffectiveClass(this).hashCode();
  }
}
