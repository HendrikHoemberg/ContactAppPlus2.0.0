package controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import contactApp.Contact;
import service.ContactService;

/**
 * REST controller for managing contacts.
 * Provides endpoints for CRUD operations on contacts.
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    /**
     * Constructor for ContactController.
     * 
     * @param contactService The service for contact operations, injected by Spring
     */
    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Retrieves all contacts from the database.
     * 
     * @return A list of all contacts
     */
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    /**
     * Retrieves a specific contact by its ID.
     * 
     * @param id The ID of the contact to retrieve
     * @return ResponseEntity containing the contact if found, or a 404 Not Found response
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new contact.
     * 
     * @param contact The contact details to create
     * @return The created contact with its generated ID
     */
    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    /**
     * Updates an existing contact.
     * 
     * @param id             The ID of the contact to update
     * @param contactDetails The new details for the contact
     * @return ResponseEntity containing the updated contact if found, or a 404 Not Found response
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contactDetails) {
        Optional<Contact> optionalContact = contactService.getContactById(id);
        
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setFirstName(contactDetails.getFirstName());
            contact.setLastName(contactDetails.getLastName());
            contact.setPhoneNumber(contactDetails.getPhoneNumber());
            contact.setEmail(contactDetails.getEmail());
            contact.setAddress(contactDetails.getAddress());
            contact.setDateOfBirth(contactDetails.getDateOfBirth());
            
            return ResponseEntity.ok(contactService.updateContact(contact));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a contact by its ID.
     * 
     * @param id The ID of the contact to delete
     * @return ResponseEntity with no content if successful, or a 404 Not Found response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        
        if (contact.isPresent()) {
            contactService.deleteContact(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}