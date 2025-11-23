package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibstraining.courses.spring.springfudamentals9.dao.ClientRepository;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ActiveAccountNotSet;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount;

import java.util.ArrayList;
import java.util.Optional;
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

  public void removeAccount(Client client, Class<? extends Account<?>> accountType) {
    client.setAccounts(client.getAccounts().stream()
                     .filter(a -> a.getClass() != accountType)
                     .collect(Collectors.toCollection(ArrayList::new)));

    updateClient(client);
  }

  @SuppressWarnings("unused")
  public void setAccounts(Client client, Set<? extends Account<?>> accounts) {
    client.getAccounts().clear();
    client.getAccounts().addAll(accounts);
    updateClient(client);
  }

  @Transactional
  public void setDefaultActiveAccountIfNotSet(Client client) {

    // Загружаем управляемую сущность (managed entity), чтобы инициализировать ленивую коллекцию accounts
    val managedClient = repository.findById(client.getId()).orElseThrow();
//    val accounts = managedClient.getAccounts(); // initialize lazy collection
    val activeAccount =
        Optional.<Account<?>>ofNullable(client.getActiveAccount())
                .or(() -> managedClient.getAccount(CheckingAccount.class))
                .or(() -> managedClient.getAccount(SavingAccount.class))
                .orElseThrow(() -> new ActiveAccountNotSet(client.getName()));

    // set on both the managed entity and the provided instance
    managedClient.setActiveAccount(activeAccount);
    client.setActiveAccount(activeAccount);

    updateClient(managedClient);

    println("Default account set for %s: %s".formatted(
        client.getName(),
        activeAccount.getClass().getSimpleName()));
  }

  @Locked.Write
  private void updateClient(Client client) {
    repository.save(client);
  }
}
