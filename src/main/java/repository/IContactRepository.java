package repository;

import java.util.List;
import java.util.Optional;

import contactApp.Contact;

public interface IContactRepository {
    Contact addContact(Contact contact);

    List<Contact> getAllContacts();

    Optional<Contact> getContactById(Long id);

    Contact updateContact(Contact contact);

    void deleteContact(Long id);
}