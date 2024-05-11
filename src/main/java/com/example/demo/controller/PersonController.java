package com.example.demo.controller;

import com.example.demo.dto.PersonInput;
import com.example.demo.model.Person;
import com.example.demo.model.PersonRole;
import com.example.demo.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PersonController {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonController(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/persons")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity createNewPerson(@RequestBody PersonInput personInput) {
        String hashedPassword = passwordEncoder.encode(personInput.getPassword());
        Person newPerson = new Person(personInput.getUsername(), hashedPassword, PersonRole.USER);
        try {
            personRepository.save(newPerson);
            log.info("Created new person");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(path = "/persons")
    public ResponseEntity<Page<Person>> getAllPersons(Pageable pageable) {
        log.info("Fetching all persons with pagination");
        Page<Person> persons = personRepository.findAll(pageable);
        return ResponseEntity.ok(persons);
    }
}
