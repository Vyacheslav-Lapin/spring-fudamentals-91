package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibstraining.courses.spring.springfudamentals9.aop.Loggable;
import ru.ibstraining.courses.spring.springfudamentals9.dao.ClientRepository;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.AccountNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ClientNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static ru.ibstraining.courses.spring.springfudamentals9.aop.Loggable.LogLevel.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount.*;

public interface Banking {

  Client addClient(Client client);

  Client getClient(String name);

  Stream<Client> getClients();

  void deleteClient(Client client);

  <T extends Account<T>> T createAccount(Client client, Class<? extends T> type);

  <T extends Account<T>> T createAccount(UUID clientId, Class<? extends T> type);

  void updateAccount(Client c, Account<?> account);

  <T extends Account<T>> T getAccount(Client client, Class<? extends T> type);

  Stream<Account<?>> getAllAccounts();

  default Stream<Account<?>> getAllAccounts(Client client) {
    return getAllAccounts(client.getId());
  }

  Stream<Account<?>> getAllAccounts(UUID clientId);

  void transferMoney(Client from, Client to, double amount);
}

@Service
@Transactional
@Loggable(WARN)
@RequiredArgsConstructor
class BankingImpl implements Banking {

  private final ClientRepository repository;
  private final ClientService clientService;

  @Override
  public Client addClient(Client c) {
    return repository.save(c);
  }

  @Override
  public Client getClient(String name) {
    return repository.getByName(name)
                     .orElseThrow(() -> new ClientNotFoundException(name));
  }

  @Override
  public Stream<Client> getClients() {
    return repository.findAllAsStream();
  }

  @Override
  public void deleteClient(Client c) {
    repository.deleteById(c.getId());
  }

  @Override
  public <T extends Account<T>> T createAccount(Client client, Class<? extends T> type) {
    return createAccount(client.getId(), type);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Account<T>> T createAccount(UUID clientId, Class<? extends T> type) {

    val client = repository.findById(clientId)
                           .orElseThrow(() -> new RuntimeException("Account not found!"));

    val account = type == CheckingAccount.class ? CheckingAccount(0) : SavingAccount(0);
    client.addAccount(account);
    repository.save(client);
    return (T) account;
  }

  @Override
  @SuppressWarnings({"java:S1905", "unchecked"})
  public void updateAccount(Client c, Account<?> account) {
    repository.findById(c.getId())
              .ifPresent(clientToUpdate -> {
                clientService.removeAccount(clientToUpdate, (Class<Account<?>>) account.getClass());
                clientToUpdate.addAccount(account);
                repository.save(clientToUpdate);
              });
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Account<T>> T getAccount(Client client, Class<? extends T> type) {
    return (T) client.getAccounts().stream()
                     .filter(a -> a.getClass() == type)
                     .findFirst()
                     .orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Stream<Account<?>> getAllAccounts() {
    return repository.findAllAsStream()
                     .map(Client::getAccounts)
                     .flatMap(Collection::stream);
  }

  @Override
  public Stream<Account<?>> getAllAccounts(UUID clientId) {
    return repository.findById(clientId)
                     .map(Client::getAccounts)
                     .orElseThrow(() -> new ClientNotFoundException(repository.findById(clientId)
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
