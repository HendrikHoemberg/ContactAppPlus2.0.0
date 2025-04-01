package service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import contactApp.Contact;
import repository.IContactRepository;

@Service
public class ContactService {

    private final IContactRepository contactRepository;

    @Autowired
    public ContactService(@Qualifier("jdbcContactRepository") IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.getAllContacts();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.getContactById(id);
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteContact(id);
    }
}