package ru.ibstraining.courses.spring.springfudamentals9.model;

import lombok.experimental.NonFinal;

import java.util.Objects;


public interface AbstractAccount {

  long getId();

  double getBalance();
  AbstractAccount stBalance(double balance);

  default void deposit(double amount) {
    if (amount < 0) return;
    setBalance(amount);
  }

  void withdraw(double amount);

  @Override
  boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractAccount that = (AbstractAccount) o;
    return id == that.id &&
           Double.compare(that.balance, balance) == 0;
  }

  @Override
  int hashCode() {
    return Objects.hash(id, balance);
  }

  @Override
  String toString() {

    StringBuilder builder = new StringBuilder();
    builder
        .append("\n")
        .append("\n\tbalance = ")
        .append(balance);

    return builder.toString();
  }

  double getBalance() {
    return balance;
  }

  void setBalance(double balance) {
    this.balance = balance;
  }

  long getId() {
    return id;
  }

  void setId(long id) {
    this.id = id;
  }
}
