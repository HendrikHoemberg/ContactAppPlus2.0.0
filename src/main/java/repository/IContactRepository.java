package repository;

import java.util.List;
import java.util.Optional;

import contactApp.Contact;

public interface IContactRepository {
    void addContact(Contact contact);
    List<Contact> getAllContacts();
    Optional<Contact> getContactById(Long id);
    void updateContact(Contact contact);
    void deleteContact(Long id);
    Contact save(Contact contact);
}