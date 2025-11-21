package ru.ibstraining.courses.spring.springfudamentals9.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ClientRepository extends JpaRepository<Client, UUID> {

  //    Client add(Client client);
//
//    Optional<Client> get(UUID id);
//
//    Optional<Client> getBy(String name);
//
  Stream<Client> getAll();

  Optional<Client> getBy(String name);
//
//    Client update(Client client);
//
//    boolean remove(UUID id);
}
