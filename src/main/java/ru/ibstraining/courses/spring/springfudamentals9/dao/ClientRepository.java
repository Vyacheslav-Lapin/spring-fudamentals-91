package ru.ibstraining.courses.spring.springfudamentals9.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ibstraining.courses.spring.springfudamentals9.model.Client;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ClientRepository extends JpaRepository<Client, UUID> {

//  <S extends Client> S save(@NotNull S entity);

//  Optional<Client> findById(@NotNull UUID id);

//  void deleteById(UUID id);

  Optional<Client> getByName(String name);

  @Query("select c from Client c")
  Stream<Client> findAllAsStream();
}
