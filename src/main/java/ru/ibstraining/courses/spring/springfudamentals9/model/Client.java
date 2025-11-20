package ru.ibstraining.courses.spring.springfudamentals9.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jspecify.annotations.Nullable;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.AccountNumberLimitException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Client {

  public static final int ACCOUNT_LIMIT_FOR_CLIENT = 2;
  UUID id = UUID.randomUUID();

  @NonNull String name;

  /**
   * Длина не должна превышать {@code ACCOUNT_LIMIT_FOR_CLIENT}
   */
  List<Account> accounts = new ArrayList<>();

  @NonNull Gender gender;

  @NonNull String city = "Moscow";

  Account activeAccount;

  public boolean checkIfActiveAccountSet() {
    return activeAccount != null;
  }

  @SuppressWarnings("unchecked")
  public <T extends Account> Optional<T> getAccount(Class<? extends T> type) {
    for (val account : accounts) {
      if (account.getClass().equals(type)) {
        return Optional.of((T) account);
      }
    }

    return Optional.empty();
  }

  public void addAccount(@Nullable Account account) {

    if (accounts.size() >= ACCOUNT_LIMIT_FOR_CLIENT)
      throw new AccountNumberLimitException();

    if (account != null)
      accounts.add(account);
  }

  @Getter
  @RequiredArgsConstructor
  public enum Gender {
    MALE("Mr"),
    FEMALE("Ms"),
    UNDEFINED("");

    final String prefix;
  }
}
