package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ActiveAccountNotSet;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount;

import java.util.Set;

import static java.lang.IO.*;

@Service
@RequiredArgsConstructor
public class ClientService {
  ClientRepository repository;

  @Locked.Read
  public double getBalance(Client client) {
    if (!client.checkIfActiveAccountSet())
      throw new ActiveAccountNotSet(client.getName());
    return client.getActiveAccount().getBalance();
  }

  public void deposit(Client client, double amount) {
    if (!client.checkIfActiveAccountSet()) {
      throw new ActiveAccountNotSet(client.getName());
    }

    client.getActiveAccount().deposit(amount);
    updateClient(client);
  }

  public void withdraw(Client client, double amount) {
    if (!client.checkIfActiveAccountSet()) {
      throw new ActiveAccountNotSet(client.getName());
    }

    client.getActiveAccount().withdraw(amount);
    updateClient(client);
  }

  public void removeAccount(Client client, Class<? extends Account> accountType) {
    client.setAccounts(client.getAccounts().stream()
                     .filter(a -> a.getClass() != accountType)
                     .toList());

    updateClient(client);
  }

  public void setAccounts(Client client, Set<? extends Account> accounts) {
    client.getAccounts().clear();
    client.getAccounts().addAll(accounts);
    updateClient(client);
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void setDefaultActiveAccountIfNotSet(Client client) {

    val accounts = client.getAccounts();
    val activeAccount = client.getActiveAccount();

    if (activeAccount == null && !accounts.isEmpty()) {
      Account account = client.<Account>getAccount(CheckingAccount.class)
          .or(() -> client.<Account>getAccount(SavingAccount.class))
          .get();

      client.setActiveAccount(account);

      updateClient(client);

      println("Default account set for %s: %s".formatted(
          client.getName(),
          activeAccount.getClass().getSimpleName()));
    }
  }

  @Locked.Write
  private void updateClient(Client client) {
    repository.update(client);
  }
}
