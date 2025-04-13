package br.com.integrahub.services;

import br.com.integrahub.dtos.crm.contact.ContactRequest;
import br.com.integrahub.dtos.crm.contact.ContactResponse;

public interface ContactService {
    ContactResponse create(ContactRequest contact);

    ContactResponse get(String id);
}
