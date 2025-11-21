package ru.ibstraining.courses.spring.springfudamentals9.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.ibstraining.courses.spring.springfudamentals9.model.Account;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client.Gender;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@Validated
@Jacksonized
public class ClientDto {
  UUID id;
  @Size(min = 2, max = 50) String name;
  List<Account> accounts;
  Gender gender;
  String city;
  Account activeAccount;

  public static ClientDto from(Client client) {
    return ClientDto.builder()
                    .id(client.getId())
                    .name(client.getName())
                    .accounts(client.getAccounts())
                    .gender(client.getGender())
                    .city(client.getCity())
                    .activeAccount(client.getActiveAccount()).build();
  }

  public Client toClient() {
    return new Client(name, gender)
        .setAccounts(accounts)
        .setId(id)
        .setCity(city)
        .setActiveAccount(activeAccount);
  }
}
