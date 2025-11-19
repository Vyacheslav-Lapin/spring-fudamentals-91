package ru.ibstraining.courses.spring.springfudamentals9;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ActiveAccountNotSet;

import static java.lang.IO.*;

@SuppressWarnings("java:S1118")
@SpringBootApplication
public class SpringFudamentals9Application {

  private static final String[] CLIENT_NAMES = {
      "Jonny Bravo",
      "Adam Budzinski",
      "Anna Smith",
      };

  static void main(String[] args) {
    SpringApplication.run(SpringFudamentals9Application.class, args);
  }

  @Bean
  ApplicationRunner runner() {
    return __ -> {
      ClientRepository repository = new MapClientRepository();
      Banking banking = initialize(repository);

      workWithExistingClients(banking);

      bankingServiceDemo(banking);

//        bankReportsDemo(repository);
    };
  }

  static void bankReportsDemo(ClientRepository repository) {
    println("\n=== Using BankReportService ===\n");

    BankReportService reportService = new BankReportServiceImpl();
    reportService.setRepository(repository);

    println("Number of clients: " + reportService.getNumberOfBankClients());

    println("Number of accounts: " + reportService.getAccountsNumber());

    println("Bank Credit Sum: " + reportService.getBankCreditSum());
  }


  public static void bankingServiceDemo(Banking banking) {

    println("\n=== Initialization using Banking implementation ===\n");

    Client anna = new Client(CLIENT_NAMES[2], Gender.FEMALE);
    anna = banking.addClient(anna);

    AbstractAccount saving = banking.createAccount(anna, SavingAccount.class);
    saving.deposit(1000);

    banking.updateAccount(anna, saving);

    AbstractAccount checking = banking.createAccount(anna, CheckingAccount.class);
    checking.deposit(3000);

    banking.updateAccount(anna, checking);

    banking.getAllAccounts(anna).stream().forEach(IO::println);
  }

  public static void workWithExistingClients(Banking banking) {

    println("\n=======================================");
    println("\n===== Work with existing clients ======");

    Client jonny = banking.getClient(CLIENT_NAMES[0]);

    try {

      jonny.deposit(5_000);

    } catch (ActiveAccountNotSet e) {

      System.out.println(e.getMessage());

      jonny.setDefaultActiveAccountIfNotSet();
      jonny.deposit(5_000);
    }

    println(jonny);

    Client adam = banking.getClient(CLIENT_NAMES[1]);
    adam.setDefaultActiveAccountIfNotSet();

    adam.withdraw(1500);

    double balance = adam.getBalance();
    println("\n" + adam.getName() + ", current balance: " + balance);

    banking.transferMoney(jonny, adam, 1000);

    println("\n=======================================");
    banking.getClients().forEach(IO::println);
  }

  /*
   * Method that creates a few clients and initializes them with sample values
   */
  public static Banking initialize(ClientRepository repository) {

    Banking banking = new BankingImpl();
    banking.setRepository(repository);

    Client client_1 = new Client(CLIENT_NAMES[0], Gender.MALE);

    AbstractAccount savingAccount = new SavingAccount(1000);
    client_1.addAccount(savingAccount);

    AbstractAccount checkingAccount = new CheckingAccount(1000);
    client_1.addAccount(checkingAccount);

    Client client_2 = new Client(CLIENT_NAMES[1], Gender.MALE);

    AbstractAccount checking = new CheckingAccount(1500);
    client_2.addAccount(checking);

    banking.addClient(client_1);
    banking.addClient(client_2);

    return banking;
  }

}
