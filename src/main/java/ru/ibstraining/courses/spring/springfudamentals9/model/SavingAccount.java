package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.NotEnoughFundsException;

import java.util.Objects;

import static lombok.AccessLevel.*;
import static ru.ibstraining.courses.spring.springfudamentals9.commons.HibernateUtils.*;

@SuppressWarnings({"com.intellij.jpb.LombokDataInspection",
                   "com.haulmont.ampjpb.LombokDataInspection"})

@Data
@Entity
@DiscriminatorValue("SAVING")
@NoArgsConstructor(access = PROTECTED)
public class SavingAccount extends Account<SavingAccount> {

  @SuppressWarnings({"java:S112", "java:S100", "java:S1223", "MethodNameSameAsClassName"})
  public static SavingAccount SavingAccount(double initialBalance) {
    if (initialBalance < 0)
      throw new RuntimeException("Баланс должен быть больше нуля!");

    return new SavingAccount()
        .setBalance(initialBalance);
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
