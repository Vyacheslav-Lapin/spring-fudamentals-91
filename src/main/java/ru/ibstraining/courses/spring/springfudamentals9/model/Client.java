package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static lombok.AccessLevel.*;
import static ru.ibstraining.courses.spring.springfudamentals9.commons.HibernateUtils.*;

@SuppressWarnings({"com.intellij.jpb.LombokDataInspection",
                   "com.haulmont.ampjpb.LombokDataInspection"})

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Client {

  @Id
  @GeneratedValue
  UUID id;

  @NonNull String name;

  @OneToMany
  List<Account> accounts;

  @NonNull Gender gender;

  @NonNull String city = "Moscow";

  Account activeAccount;

  public boolean checkIfActiveAccountSet() {
    return activeAccount != null;
  }

  @SuppressWarnings("unchecked")
  public <T extends Account> Optional<T> getAccount(Class<? extends T> type) {
    for (val account : accounts)
      if (account.getClass().equals(type))
        return Optional.of((T) account);

    return Optional.empty();
  }

  public void addAccount(@Nullable Account account) {

//    if (accounts.size() >= ACCOUNT_LIMIT_FOR_CLIENT)
//      throw new AccountNumberLimitException();

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

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof Client client
                        && getEffectiveClass(this) == getEffectiveClass(client)
                        && getId() != null
                        && Objects.equals(getId(), client.getId());
  }

  @Override
  public int hashCode() {
    return getEffectiveClass(this).hashCode();
  }
}
