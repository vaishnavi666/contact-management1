package com.example.demo.controller;

import com.example.demo.exception.ContactFoundException;
import com.example.demo.exception.ContactNotFoundException;
import com.example.demo.model.Contact;
import com.example.demo.repositories.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ContactController {
    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {

        this.contactRepository = contactRepository;
    }

    @GetMapping(path = "/contacts")
    public Page<Contact> getAllContacts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all contacts with pagination");
        Pageable pageable = PageRequest.of(page, size);
        return contactRepository.findAll(pageable);
    }


    @GetMapping(path = "/contacts/{id}")
    public ResponseEntity contactById(@PathVariable Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isEmpty()) {
            log.error("Found no contact with id : " +id);
            throw new ContactNotFoundException("Found no contact with id : " +id);
        }
        log.info("Contact found with id : " +id);
        return ResponseEntity.ok().body(contactOptional.get());
    }

    @PostMapping(path = "/contacts")
    public ResponseEntity createContact(@RequestBody Contact newContactInput) {
        List<Contact> contactList = contactRepository.findByName(newContactInput.getName());
        if(!contactList.isEmpty()){
            log.error("Contact already exists with name : " +newContactInput.getName());
            throw new ContactFoundException("Contact already exists with name : " +newContactInput.getName());
        }
        Contact newContact = new Contact();
        newContact.setName(newContactInput.getName());
        newContact.setAddress(newContactInput.getAddress());
        newContact.setPhoneNumber(newContactInput.getPhoneNumber());
        try {
            Contact contact = contactRepository.save(newContact);
            log.info("Contact created successfully with name : " +newContactInput.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(contact);
        } catch (Exception e) {
            log.error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(path = "/contacts/update")
    public ResponseEntity updateContact(@RequestBody Contact newContactInput) {
        Optional<Contact> contactList = contactRepository.findById(newContactInput.getId().longValue());
        if (!contactList.isEmpty()) {
            try {
                Contact contact = contactRepository.save(newContactInput);
                return ResponseEntity.status(HttpStatus.CREATED).body(contact);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            throw new ContactNotFoundException("Found no contact with id : " +newContactInput.getId());
        }
    }
    @DeleteMapping(path = "/contacts/{id}")
    public ResponseEntity deleteContact(@PathVariable Long id) {
        Optional<Contact> contactList = contactRepository.findById(id);
        if(!contactList.isEmpty()){
            contactRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new ContactNotFoundException("Found no contact with ID : " +id);
    }
}
