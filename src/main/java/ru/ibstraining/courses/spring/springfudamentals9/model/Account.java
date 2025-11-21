package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.OverDraftLimitExceededException;

import java.util.UUID;

@Entity
@Getter
public abstract sealed class Account permits CheckingAccount, SavingAccount {

  @Id
  @GeneratedValue
  UUID id;

  public abstract double getBalance();
  public abstract Account setBalance(double balance);

  public void deposit(double amount) {
    if (amount < 0) return;
    setBalance(amount);
  }

  public abstract void withdraw(double amount) throws OverDraftLimitExceededException;
}
