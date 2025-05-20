package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import contactApp.Contact;
import service.ContactService;

class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private Contact sampleContact;

    private long id = 42;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleContact = new Contact();
        sampleContact.setId(id);
        sampleContact.setFirstName("Rainer");
        sampleContact.setLastName("Winkler");
        sampleContact.setPhoneNumber("1234567890");
        sampleContact.setEmail("rainer.winkler@younow.de");
        sampleContact.setAddress("Altschauerberg 8 91448 Emskirchen");
        sampleContact.setDateOfBirth("02.08.1989");
    }

    @Test
    void testGetAllContacts() {
        when(contactService.getAllContacts()).thenReturn(List.of(sampleContact));
        List<Contact> contacts = contactController.getAllContacts();
        assertEquals(1, contacts.size());
        verify(contactService).getAllContacts();
    }

    @Test
    void testGetContactById_found() {
        when(contactService.getContactById(id)).thenReturn(Optional.of(sampleContact));
        ResponseEntity<Contact> response = contactController.getContactById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleContact, response.getBody());
    }

    @Test
    void testGetContactById_notFound() {
        when(contactService.getContactById(id)).thenReturn(Optional.empty());
        ResponseEntity<Contact> response = contactController.getContactById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateContact() {
        when(contactService.addContact(any(Contact.class))).thenReturn(sampleContact);
        Contact created = contactController.createContact(sampleContact);
        assertEquals(sampleContact, created);
    }

    @Test
    void testUpdateContact_found() {
        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Rudi");
        updatedContact.setLastName("Winkler");
        updatedContact.setPhoneNumber("1234567890");
        updatedContact.setEmail("rudi.aus@buddeln.de");
        updatedContact.setAddress("St2244 38 91448 Emskirchen");
        updatedContact.setDateOfBirth("04.07.1942");

        when(contactService.getContactById(id)).thenReturn(Optional.of(sampleContact));
        when(contactService.updateContact(any(Contact.class))).thenReturn(updatedContact);

        ResponseEntity<Contact> response = contactController.updateContact(id, updatedContact);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rudi", response.getBody().getFirstName());
        ResponseEntity<Contact> responseId = contactController.getContactById(id);
        assertEquals(HttpStatus.OK, responseId.getStatusCode());
        assertEquals("Rudi", responseId.getBody().getFirstName());
    }

    @Test
    void testUpdateContact_notFound() {
        when(contactService.getContactById(id)).thenReturn(Optional.empty());
        ResponseEntity<Contact> response = contactController.updateContact(id, new Contact());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteContact_found() {
        when(contactService.getContactById(id)).thenReturn(Optional.of(sampleContact));
        doNothing().when(contactService).deleteContact(id);
        ResponseEntity<Void> response = contactController.deleteContact(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteContact_notFound() {
        when(contactService.getContactById(id)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = contactController.deleteContact(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}