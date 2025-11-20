package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.ibstraining.courses.spring.springfudamentals9.dao.ClientRepository;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.AccountNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ClientNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount.*;

public interface Banking {

  Client addClient(Client client);

  Client getClient(String name);

  Stream<Client> getClients();

  void deleteClient(Client client);

  default <T extends Account> T createAccount(Client client, Class<? extends T> type) {
    return createAccount(client.getId(), type);
  }
  <T extends Account> T createAccount(UUID clientId, Class<? extends T> type);

  void updateAccount(Client c, Account account);

  <T extends Account> T getAccount(Client client, Class<? extends T> type);

  Stream<Account> getAllAccounts();

  default Stream<Account> getAllAccounts(Client client) {
    return getAllAccounts(client.getId());
  }

  Stream<Account> getAllAccounts(UUID clientId);

  void transferMoney(Client from, Client to, double amount);
}

@Component
@RequiredArgsConstructor
class BankingImpl implements Banking {

  ClientRepository repository;
  ClientService clientService;

  @Override
  public Client addClient(Client c) {
    return repository.add(c);
  }

  @Override
  public Client getClient(String name) {
    return repository.getBy(name)
                     .orElseThrow(() -> new ClientNotFoundException(name));
  }

  @Override
  public Stream<Client> getClients() {
    return repository.getAll();
  }

  @Override
  public void deleteClient(Client c) {
    repository.remove(c.getId());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Account> T createAccount(UUID clientId, Class<? extends T> type) {

    val client = repository.get(clientId)
                           .orElseThrow(() -> new RuntimeException("Account not found!"));

    val account = type == CheckingAccount.class ? CheckingAccount(0) : SavingAccount(0);
    client.addAccount(account);
    repository.update(client);
    return (T) account;
  }

  @Override
  public void updateAccount(Client c, Account account) {
    repository.get(c.getId())
              .ifPresent(clientToUpdate -> {
                clientService.removeAccount(clientToUpdate, account.getClass());
                clientToUpdate.addAccount(account);
                repository.update(c);
              });
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Account> T getAccount(Client client, Class<? extends T> type) {
    return (T) client.getAccounts().stream()
                     .filter(a -> a.getClass() == type)
                     .findFirst()
                     .orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Stream<Account> getAllAccounts() {
    return repository.getAll()
                     .map(Client::getAccounts)
                     .flatMap(Collection::stream);
  }

  @Override
  public Stream<Account> getAllAccounts(UUID clientId) {
    return repository.get(clientId)
                     .map(Client::getAccounts)
                     .orElseThrow(() -> new ClientNotFoundException(repository.get(clientId)
                                                                              .map(Client::getName)
                                                                              .orElseThrow()))
                     .stream();
  }

  @Override
  @Locked.Write
  public void transferMoney(Client from, Client to, double amount) {
    clientService.withdraw(from, amount);
    clientService.deposit(to, amount);
  }
}
