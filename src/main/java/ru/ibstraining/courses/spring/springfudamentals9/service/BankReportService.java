package ru.ibstraining.courses.spring.springfudamentals9.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ibstraining.courses.spring.springfudamentals9.dao.ClientRepository;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface BankReportService {
    long getNumberOfBankClients();
    long getAccountsNumber();
    Stream<Client> getClientsSorted();
    double getBankCreditSum();
    Map<String, List<Client>> getClientsByCity();
}

@Component
@RequiredArgsConstructor//(onConstructor_ = @Autowired)
class BankReportServiceImpl implements BankReportService {

  ClientRepository repository;

  @Override
  public long getNumberOfBankClients() {
    return repository.findAll().size();
  }

  @Override
  public long getAccountsNumber() {
    return repository.findAll().stream()
                     .mapToLong(c -> c.getAccounts().size())
                     .sum();
  }

  @Override
  public Stream<Client> getClientsSorted() {
    return repository.findAll().stream()
                     .sorted(Comparator.comparing(Client::getName));
  }

  @Override
  public double getBankCreditSum() {
    return repository.findAll().stream()
                     .flatMap(client -> client.getAccounts().stream())
                     .filter(account -> account.getClass() == CheckingAccount.class)
                     .mapToDouble(Account::getBalance)
                     .filter(balance -> balance < 0)
                     .sum();
  }

  @Override
  public Map<String, List<Client>> getClientsByCity() {
    return repository.findAll().stream()
                     .collect(Collectors.groupingBy(Client::getCity));
  }
}
