package ru.ibstraining.courses.spring.springfudamentals9.dao;

import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ClientRepository {

    Client add(Client client);

    Optional<Client> get(UUID id);

    Optional<Client> getBy(String name);

    Stream<Client> getAll();

    Client update(Client client);

    boolean remove(UUID id);
}
