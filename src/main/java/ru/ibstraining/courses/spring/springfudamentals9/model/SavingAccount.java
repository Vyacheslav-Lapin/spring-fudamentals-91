package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.NotEnoughFundsException;

import java.util.Objects;

import static lombok.AccessLevel.*;
import static ru.ibstraining.courses.spring.springfudamentals9.commons.HibernateUtils.*;

@SuppressWarnings({"com.intellij.jpb.LombokDataInspection", "com.haulmont.ampjpb.LombokDataInspection"})

@Data
@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(access = PRIVATE)
public final class SavingAccount extends Account {

  @NonNull double balance;

  @SuppressWarnings({"java:S112", "MethodNameSameAsClassName"})
    public static SavingAccount SavingAccount(double initialBalance) {
        if (initialBalance < 0)
          throw new RuntimeException("Баланс должен быть больше нуля!");
      return new SavingAccount(initialBalance);
    }

    @Override
    public void withdraw(double amount) {
        if (getBalance() < amount)
          throw new NotEnoughFundsException(amount);

        setBalance(getBalance() - amount);
    }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof SavingAccount savingAccount
                        && getEffectiveClass(this) == getEffectiveClass(savingAccount)
                        && getId() != null
                        && Objects.equals(getId(), savingAccount.getId());
  }

  @Override
  public int hashCode() {
    return getEffectiveClass(this).hashCode();
  }
}
