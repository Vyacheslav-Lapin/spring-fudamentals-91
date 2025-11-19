package ru.ibstraining.courses.spring.springfudamentals9.service;

import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.service.storage.ClientRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface Banking {

  Client addClient(Client client);

  Client getClient(String name);

  Stream<Client> getClients();

  void deleteClient(Client client);

  <T extends Account> T createAccount(UUID clientId, Class<? extends T> type);

  void updateAccount(Client c, Account account);

  <T extends Account> T getAccount(Client client, Class<? extends T> type);

  <T extends Account> List<T> getAllAccounts();

  <T extends Account> List<T> getAllAccounts(Client client);

  void transferMoney(Client from, Client to, double amount);

  //todo 19.11.2025: разобраться - зачем это тут и прибить!
  void setRepository(ClientRepository storage);
}
