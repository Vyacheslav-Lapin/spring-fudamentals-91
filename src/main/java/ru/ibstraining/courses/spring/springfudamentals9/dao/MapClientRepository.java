package ru.ibstraining.courses.spring.springfudamentals9.dao;

import org.springframework.stereotype.Component;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class MapClientRepository implements ClientRepository {

  Map<UUID, Client> data = new HashMap<>();

  @Override
  public Client add(Client client) {
    data.put(client.getId(), client);
    return client;
  }

  @Override
  public Optional<Client> get(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Stream<Client> getAll() {
    return data.values().stream();
  }

  @Override
  public Optional<Client> getBy(String name) {
    return getAll()
        .filter(client -> client.getName().equals(name))
        .findFirst();
  }

  @Override
  public Client update(Client client) {
    return data.put(client.getId(), client);
  }

  @Override
  public boolean remove(UUID id) {
    return data.remove(id) != null;
  }
}
