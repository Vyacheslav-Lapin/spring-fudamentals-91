package ru.ibstraining.courses.spring.springfudamentals9;

import lombok.val;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ActiveAccountNotSet;
import ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount;
import ru.ibstraining.courses.spring.springfudamentals9.service.BankReportService;
import ru.ibstraining.courses.spring.springfudamentals9.service.Banking;
import ru.ibstraining.courses.spring.springfudamentals9.service.ClientService;

import static java.lang.IO.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.CheckingAccount.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.Client.Gender.*;
import static ru.ibstraining.courses.spring.springfudamentals9.model.SavingAccount.*;

@SuppressWarnings("java:S1118")
@SpringBootApplication
public class BankApplication {

  private static final String[] CLIENT_NAMES = {
      "Jonny Bravo",
      "Adam Budzinski",
      "Anna Smith",
      };

  static void main(String[] args) {
    SpringApplication.run(BankApplication.class, args);
  }

  @Bean
  ApplicationRunner runner(Banking banking,
                           ClientService clientService,
                           BankReportService bankReportService,
                           TransactionTemplate transactionTemplate) {

    return _ -> transactionTemplate.executeWithoutResult(_ -> {
      initialize(banking);
      workWithExistingClients(banking, clientService);
      bankingServiceDemo(banking);
      bankReportsDemo(bankReportService);
    });
  }

  static void bankReportsDemo(BankReportService reportService) {
    println("\n=== Using BankReportService ===\n");
    println("Number of clients: " + reportService.getNumberOfBankClients());
    println("Number of accounts: " + reportService.getAccountsNumber());
    println("Bank Credit Sum: " + reportService.getBankCreditSum());
  }

  static void bankingServiceDemo(Banking banking) {
    println("\n=== Initialization using Banking implementation ===\n");
    val anna = new Client(CLIENT_NAMES[2], FEMALE);
    banking.addClient(anna);

    val saving = banking.createAccount(anna, SavingAccount.class);
    saving.deposit(1_000);
    banking.updateAccount(anna, saving);

    val checking = banking.createAccount(anna, CheckingAccount.class);
    checking.deposit(3_000);
    banking.updateAccount(anna, checking);

    banking.getAllAccounts(anna)
           .forEach(IO::println);
  }

  static void workWithExistingClients(Banking banking, ClientService clientService) {

    println("\n=======================================");
    println("\n===== Work with existing clients ======");

    val jonny = banking.getClient(CLIENT_NAMES[0]);
    int amount = 5_000;

    try {
      clientService.deposit(jonny, amount);
    } catch (ActiveAccountNotSet e) {
      println(e.getMessage());
      clientService.setDefaultActiveAccountIfNotSet(jonny);
      clientService.deposit(jonny, amount);
    }

    println(jonny);

    val adam = banking.getClient(CLIENT_NAMES[1]);
    clientService.setDefaultActiveAccountIfNotSet(adam);

    clientService.withdraw(adam, 1_500);

    val balance = clientService.getBalance(adam);
    println("\n%s, current balance: %s".formatted(adam.getName(), balance));

    banking.transferMoney(jonny, adam, 1_000);
    println("clientService.getBalance(adam) = " + clientService.getBalance(adam));

    println("\n=======================================");
    banking.getClients()
           .forEach(IO::println);
  }

  /**
   * Method that creates a few clients and initializes them with sample values
   */
  static Banking initialize(Banking banking) {

    val jonny = new Client(CLIENT_NAMES[0], MALE);
    jonny.addAccount(SavingAccount(1_000));
    jonny.addAccount(CheckingAccount(1_000));
    banking.addClient(jonny);

    val adam = new Client(CLIENT_NAMES[1], MALE);
    adam.addAccount(CheckingAccount(1_500));
    banking.addClient(adam);

    return banking;
  }
}
