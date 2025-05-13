function showNotification(message, type = 'success') {
    const toast = document.getElementById('notificationToast');
    const toastMessage = document.getElementById('toastMessage');
    
    toastMessage.textContent = message;
    
    toast.classList.remove('bg-success', 'bg-danger');
    toast.classList.add(type === 'success' ? 'bg-success' : 'bg-danger');
    
    window.contactToast.show();
}const contactTableBody = document.getElementById('contactTableBody');
const contactDetailsForm = document.getElementById('contactDetailsForm');
const newContactForm = document.getElementById('newContactForm');
const contactFilter = document.getElementById('contactFilter');
const clearFilterBtn = document.getElementById('clearFilter');

const editContactBtn = document.getElementById('editContactBtn');
const applyChangesBtn = document.getElementById('applyChangesBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');

const showTab = document.getElementById('show-tab');
const newTab = document.getElementById('new-tab');

let currentContactId = null;
let allContacts = []; 
let isEditMode = false;

document.addEventListener('DOMContentLoaded', () => {
    loadContacts();
    
    contactDetailsForm.addEventListener('submit', handleContactUpdate);
    newContactForm.addEventListener('submit', handleNewContact);
    
    contactFilter.addEventListener('input', filterContacts);
    clearFilterBtn.addEventListener('click', clearFilter);
    
    editContactBtn.addEventListener('click', enableEditMode);
    cancelEditBtn.addEventListener('click', disableEditMode);
    
    window.contactToast = new bootstrap.Toast(document.getElementById('notificationToast'), {
        delay: 1500
    });
});

function enableEditMode() {
    isEditMode = true;
    
    const formInputs = contactDetailsForm.querySelectorAll('input, textarea');
    formInputs.forEach(input => {
        input.readOnly = false;
    });
    
    editContactBtn.classList.add('d-none');
    applyChangesBtn.classList.remove('d-none');
    cancelEditBtn.classList.remove('d-none');
}

function disableEditMode() {
    isEditMode = false;
    
    const formInputs = contactDetailsForm.querySelectorAll('input, textarea');
    formInputs.forEach(input => {
        input.readOnly = true;
    });
    
    editContactBtn.classList.remove('d-none');
    applyChangesBtn.classList.add('d-none');
    cancelEditBtn.classList.add('d-none');
    
    if (currentContactId) {
        const currentContact = allContacts.find(contact => contact.id == currentContactId);
        if (currentContact) {
            populateContactForm(currentContact);
        }
    }
}

async function loadContacts() {
    try {
        const response = await fetch('/api/contacts');
        const contacts = await response.json();
        
        allContacts = contacts;
        renderContactTable(contacts);
        
        if (contacts.length > 0) {
            if (currentContactId) {
                const contactToSelect = contacts.find(contact => contact.id == currentContactId);
                if (contactToSelect) {
                    selectContact(contactToSelect);
                    return;
                }
            }
            selectContact(contacts[0]);
        }
    } catch (error) {
        console.error('Error loading contacts:', error);
    }
}

function renderContactTable(contacts) {
    contactTableBody.innerHTML = '';
    
    contacts.forEach(contact => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${contact.firstName}</td>
            <td>${contact.lastName}</td>
            <td>${contact.phoneNumber || ''}</td>
            <td>${contact.email || ''}</td>
            <td>${contact.address || ''}</td>
            <td>${contact.dateOfBirth || ''}</td>
            <td>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${contact.id}">
                    <i class="bi bi-trash"></i> Delete
                </button>
            </td>
        `;
        
        row.addEventListener('click', (e) => {
            if (e.target.closest('.delete-btn')) return;
            
            selectContact(contact);
        });
        
        const deleteBtn = row.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', () => deleteContact(contact.id));
        
        contactTableBody.appendChild(row);
    });
}

function filterContacts() {
    const filterValue = contactFilter.value.toLowerCase().trim();
    
    if (!filterValue) {
        renderContactTable(allContacts);
        return;
    }
    
    const filteredContacts = allContacts.filter(contact => {
        return (
            contact.firstName.toLowerCase().includes(filterValue) ||
            contact.lastName.toLowerCase().includes(filterValue) ||
            (contact.phoneNumber && contact.phoneNumber.toLowerCase().includes(filterValue)) ||
            (contact.email && contact.email.toLowerCase().includes(filterValue)) ||
            (contact.address && contact.address.toLowerCase().includes(filterValue))
        );
    });
    
    renderContactTable(filteredContacts);
    
    if (filteredContacts.length > 0) {
        if (currentContactId) {
            const currentInFiltered = filteredContacts.find(contact => contact.id == currentContactId);
            if (currentInFiltered) {
                selectContact(currentInFiltered);
                return;
            }
        }
        selectContact(filteredContacts[0]);
    } else {
        clearForms();
    }
}

function clearFilter() {
    contactFilter.value = '';
    renderContactTable(allContacts);
    
    if (allContacts.length > 0) {
        if (currentContactId) {
            const contactToSelect = allContacts.find(contact => contact.id == currentContactId);
            if (contactToSelect) {
                selectContact(contactToSelect);
                return;
            }
        }
        selectContact(allContacts[0]);
    }
}

function selectContact(contact) {
    if (isEditMode) {
        if (!confirm('You have unsaved changes. Do you want to continue?')) {
            return;
        }
        disableEditMode();
    }
    
    currentContactId = contact.id;
    
    const rows = contactTableBody.querySelectorAll('tr');
    rows.forEach(row => row.classList.remove('table-primary'));
    
    const selectedRow = Array.from(rows).find(row => 
        row.querySelector('.delete-btn').dataset.id == contact.id
    );
    
    if (selectedRow) {
        selectedRow.classList.add('table-primary');
        selectedRow.scrollIntoView({ block: 'nearest', behavior: 'smooth' });
    }
    
    populateContactForm(contact);
    
    const showTabInstance = new bootstrap.Tab(showTab);
    showTabInstance.show();
}

function populateContactForm(contact) {
    document.getElementById('contact-id').value = contact.id;
    document.getElementById('contact-first-name').value = contact.firstName;
    document.getElementById('contact-last-name').value = contact.lastName;
    document.getElementById('contact-date-of-birth').value = contact.dateOfBirth || '';
    document.getElementById('contact-address').value = contact.address || '';
    document.getElementById('contact-email').value = contact.email || '';
    document.getElementById('contact-phone').value = contact.phoneNumber || '';
}

async function handleContactUpdate(e) {
    e.preventDefault();
    
    const contactData = {
        id: document.getElementById('contact-id').value,
        firstName: document.getElementById('contact-first-name').value,
        lastName: document.getElementById('contact-last-name').value,
        phoneNumber: document.getElementById('contact-phone').value,
        email: document.getElementById('contact-email').value,
        address: document.getElementById('contact-address').value,
        dateOfBirth: document.getElementById('contact-date-of-birth').value || null
    };
    
    try {
        const response = await fetch(`/api/contacts/${contactData.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contactData)
        });
        
        if (response.ok) {
            const updatedContact = await response.json();
            currentContactId = updatedContact.id;
            loadContacts();
            showNotification('Contact updated successfully');
            disableEditMode();
        } else {
            showNotification('Failed to update contact', 'error');
        }
    } catch (error) {
        console.error('Error updating contact:', error);
        showNotification('An error occurred while updating the contact', 'error');
    }
}

async function handleNewContact(e) {
    e.preventDefault();
    
    const contactData = {
        firstName: document.getElementById('new-first-name').value,
        lastName: document.getElementById('new-last-name').value,
        phoneNumber: document.getElementById('new-phone').value,
        email: document.getElementById('new-email').value,
        address: document.getElementById('new-address').value,
        dateOfBirth: document.getElementById('new-date-of-birth').value || null
    };
    
    try {
        const response = await fetch('/api/contacts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contactData)
        });
        
        if (response.ok) {
            const newContact = await response.json();
            currentContactId = newContact.id;
            loadContacts();
            showNotification('Contact created successfully');
            
            newContactForm.reset();
            
            const showTabInstance = new bootstrap.Tab(showTab);
            showTabInstance.show();
        } else {
            showNotification('Failed to create contact', 'error');
        }
    } catch (error) {
        console.error('Error creating contact:', error);
        showNotification('An error occurred while creating the contact', 'error');
    }
}

async function deleteContact(id) {
    if (!confirm('Are you sure you want to delete this contact?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/contacts/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            if (currentContactId === id) {
                currentContactId = null;
            }
            loadContacts();
            showNotification('Contact deleted successfully');
            
            if (currentContactId === id) {
                clearForms();
            }
        } else {
            showNotification('Failed to delete contact', 'error');
        }
    } catch (error) {
        console.error('Error deleting contact:', error);
        showNotification('An error occurred while deleting the contact', 'error');
    }
}

function clearForms() {
    document.getElementById('contact-id').value = '';
    document.getElementById('contact-first-name').value = '';
    document.getElementById('contact-last-name').value = '';
    document.getElementById('contact-date-of-birth').value = '';
    document.getElementById('contact-address').value = '';
    document.getElementById('contact-email').value = '';
    document.getElementById('contact-phone').value = '';
    
    if (isEditMode) {
        disableEditMode();
    }
}