package br.com.integrahub.controllers;

import br.com.integrahub.configs.HubspotRateLimiter;
import br.com.integrahub.dtos.crm.contact.ContactRequest;
import br.com.integrahub.dtos.crm.contact.ContactResponse;
import br.com.integrahub.exceptions.DataValidationException;
import br.com.integrahub.exceptions.TooManyRequestsException;
import br.com.integrahub.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
@RestController
public class ContactController {
    private final HubspotRateLimiter hubspotRateLimiter;
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ContactRequest contact) {
        if (!hubspotRateLimiter.tryConsume()) {
            throw new TooManyRequestsException("Request limit exceeded (110/10s). Please try again later.");
        }
        log.info("Creating contact with email: {}", contact.email());
        ContactResponse response = contactService.create(contact);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + response.id()).build().toUri();
        return ResponseEntity.created(location).body("Contact created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(contactService.get(id));
    }

    @PostMapping("/webhook/contact-created")
    public ResponseEntity<Void> handleContactCreation(@RequestBody Map<String, Object>[] events) {
        for (Map<String, Object> event : events) {
            log.info("New contact creation event received. {}", event);
        }
        return ResponseEntity.noContent().build();
    }
}
