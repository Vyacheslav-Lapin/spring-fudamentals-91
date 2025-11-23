package ru.ibstraining.courses.spring.springfudamentals9.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
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

  @Column(nullable = false)
  @NonNull String name;

  @ToString.Exclude
  @OneToMany(mappedBy = "client", cascade = ALL, fetch = LAZY)
  List<Account<?>> accounts = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @NonNull Gender gender;

  @NonNull String city = "Moscow";

  @Transient
  Account<?> activeAccount;

  public boolean checkIfActiveAccountSet() {
    return activeAccount != null;
  }

  @SuppressWarnings("unchecked")
  public <T extends Account<T>> Optional<T> getAccount(Class<? extends T> type) {
    return (Optional<T>) accounts.stream()
                                 .filter(account -> account.getClass().equals(type))
                                 .findFirst();
  }

  public void addAccount(@Nullable Account<?> account) {

    if (account != null) {
      // Maintain bidirectional association for JPA
      account.setClient(this);
      accounts.add(account);
    }
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
