package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.ibstraining.courses.spring.springfudamentals9.dao.ClientRepository;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ActiveAccountNotSet;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

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

  @Locked.Write
  public void deposit(Client client, double amount) {
    if (!client.checkIfActiveAccountSet()) {
      throw new ActiveAccountNotSet(client.getName());
    }

    client.getActiveAccount().deposit(amount);
    updateClient(client);
  }

  @Locked.Write
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
                     .collect(Collectors.toCollection(ArrayList::new)));

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
    var activeAccount = client.getActiveAccount();

    if (activeAccount == null && !accounts.isEmpty()) {
      activeAccount = client.<Account>getAccount(CheckingAccount.class)
          .or(() -> client.<Account>getAccount(SavingAccount.class))
          .get();

      updateClient(client.setActiveAccount(activeAccount));

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
