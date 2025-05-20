package service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import contactApp.Contact;
import repository.IContactRepository;

/**
 * Service class for managing contacts.
 */
@Service
public class ContactService {

    private final IContactRepository contactRepository;

    /**
     * Constructor.
     * 
     * @param contactRepository The repository implementation to use for data access
     */
    @Autowired
    public ContactService(@Qualifier("ContactRepository") IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Retrieves all contacts from the repository.
     * 
     * @return A list of all contacts
     */
    public List<Contact> getAllContacts() {
        return contactRepository.getAllContacts();
    }

    /**
     * Retrieves a contact by its ID.
     * 
     * @param id The ID of the contact to retrieve
     * @return An Optional containing the contact if found, or empty if not found
     */
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.getContactById(id);
    }

    /**
     * Updates an existing contact.
     * 
     * @param contact The contact with updated information
     * @return The updated contact
     */
    public Contact updateContact(Contact contact) {
        return contactRepository.updateContact(contact);
    }

    /**
     * Deletes a contact by its ID.
     * 
     * @param id The ID of the contact to delete
     */
    public void deleteContact(Long id) {
        contactRepository.deleteContact(id);
    }

    /**
     * Adds a new contact.
     * 
     * @param contact The contact to add
     * @return The added contact 
     */
    public Contact addContact(Contact contact) {
        return contactRepository.addContact(contact);
    }
}