package ru.ibstraining.courses.spring.springfudamentals9.controllers;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ibstraining.courses.spring.springfudamentals9.dto.ClientDto;
import ru.ibstraining.courses.spring.springfudamentals9.exceptions.ClientNotFoundException;
import ru.ibstraining.courses.spring.springfudamentals9.service.Banking;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class BankingController {

  Banking banking;

  @PostMapping
  public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto client, ValidationErrors errors) {
    if (errors.getAllErrors().isEmpty()) {
      val clientDto = ClientDto.from(
          banking.addClient(client.toClient()));

      return ResponseEntity.created(URI.create("/api/clients/" + clientDto.getId()))
                           .body(clientDto);
    } else {
      throw new ValidationException(errors.toString());
    }
  }

  @GetMapping("{name:\\w+}")
  public ResponseEntity<ClientDto> getClient(@PathVariable String name) {
    try {
      val clientDto = ClientDto.from(banking.getClient(name));
      return ResponseEntity.ok(clientDto);
    } catch (ClientNotFoundException _) {
      return ResponseEntity.notFound().build();
    }
  }
}
