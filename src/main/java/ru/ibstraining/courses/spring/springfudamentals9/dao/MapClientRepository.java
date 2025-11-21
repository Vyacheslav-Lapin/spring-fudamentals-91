package ru.ibstraining.courses.spring.springfudamentals9.dao;

import org.jetbrains.annotations.NotNull;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

//@Component
public abstract class MapClientRepository {//implements ClientRepository {

  Map<UUID, Client> data = new HashMap<>();

//  @Override
  public <S extends Client> S save(@NotNull S client) {
    data.put(client.getId(), client);
    return client;
  }

//  @Override
  public Optional<Client> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

//  @Override
  public Stream<Client> getAll() {
    return data.values().stream();
  }

//  @Override
  public Optional<Client> getBy(String name) {
    return getAll()
        .filter(client -> client.getName().equals(name))
        .findFirst();
  }

//  @Override
  public void deleteById(@NotNull UUID id) {
    data.remove(id);
  }
}
