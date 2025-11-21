package ru.ibstraining.courses.spring.springfudamentals9.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.NonFinal;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.NotEnoughFundsException;

import java.util.UUID;

import static lombok.AccessLevel.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = PRIVATE)
public final class SavingAccount implements Account {

  UUID id =  UUID.randomUUID();

  @NonFinal @NonNull double balance;

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
}
