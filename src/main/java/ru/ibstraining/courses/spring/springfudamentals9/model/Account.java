package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.OverDraftLimitExceededException;

import java.util.UUID;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.InheritanceType.*;
import static lombok.AccessLevel.*;

/**
 * @param <T> concrete class type of Account
 */
@Getter
@Entity
@FieldDefaults(level = PROTECTED)
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type",
                     discriminatorType = DiscriminatorType.STRING)
public abstract class Account<T extends Account<T>> {

  @Id
  @GeneratedValue
  UUID id;

  double balance;

  // Добавляем обратную связь с клиентом
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "client_id")
  Client client;

  public void deposit(double amount) {
    if (amount < 0)
      return;

    setBalance(amount + balance);
  }

  public abstract void withdraw(double amount) throws OverDraftLimitExceededException;

  @SuppressWarnings("unchecked")
  public T setId(UUID id) {
    this.id = id;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setBalance(double balance) {
    this.balance = balance;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setClient(Client client) {
    this.client = client;
    return (T) this;
  }
}
