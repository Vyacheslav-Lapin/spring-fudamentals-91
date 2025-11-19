package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ClientNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.service.storage.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount.*;

@RequiredArgsConstructor
public class BankingImpl implements Banking {

  ClientRepository repository;

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

      val client = repository.get(clientId).orElseThrow(() -> new RuntimeException("Account not found!"))

      val account = type == CheckingAccount.class ? CheckingAccount(0) : SavingAccount(0);
      client.addAccount(account);
      repository.update(client);
      return (T) account;
    }

    @Override
    public void updateAccount(Client c, AbstractAccount account) {

        Client clientToUpdate = repository.get(c.getId());

        if (clientToUpdate != null) {

            clientToUpdate.removeAccount(account.getClass());
            clientToUpdate.addAccount(account);

            repository.update(c);
        }
    }

    @Override
    public AbstractAccount getAccount(Client c, Class type) {

        return c.getAccounts().stream()
                .filter(a -> a.getClass() == type)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException());
    }

    @Override
    public List<AbstractAccount> getAllAccounts() {

        return repository.getAll()
                .stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<AbstractAccount> getAllAccounts(Client c) {

        return new ArrayList<>(repository.get(c.getId()).getAccounts());
    }

    @Override
    public void transferMoney(Client from, Client to, double amount) {

        from.withdraw(amount);
        to.deposit(amount);
    }

    public void setRepository(ClientRepository repository) {

        this.repository = repository;
    }
}
