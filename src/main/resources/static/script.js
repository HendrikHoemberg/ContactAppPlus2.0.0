const contactTableBody = document.getElementById('contactTableBody');
const showContactForm = document.getElementById('showContactForm');
const editContactForm = document.getElementById('editContactForm');
const newContactForm = document.getElementById('newContactForm');
const contactFilter = document.getElementById('contactFilter');
const clearFilterBtn = document.getElementById('clearFilter');


const showTab = document.getElementById('show-tab');
const editTab = document.getElementById('edit-tab');
const newTab = document.getElementById('new-tab');


let currentContactId = null;
let allContacts = []; 


document.addEventListener('DOMContentLoaded', () => {
    loadContacts();
    

    editContactForm.addEventListener('submit', handleEditContact);
    newContactForm.addEventListener('submit', handleNewContact);
    
    contactFilter.addEventListener('input', filterContacts);
    clearFilterBtn.addEventListener('click', clearFilter);
});


async function loadContacts() {
    try {
        const response = await fetch('/api/contacts');
        const contacts = await response.json();
        
        allContacts = contacts;
        renderContactTable(contacts);
        

        if (contacts.length > 0) {
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
        selectContact(filteredContacts[0]);
    } else {
        clearForms();
    }
}


function clearFilter() {
    contactFilter.value = '';
    renderContactTable(allContacts);
    
    if (allContacts.length > 0) {
        selectContact(allContacts[0]);
    }
}


function selectContact(contact) {
    currentContactId = contact.id;
    

    const rows = contactTableBody.querySelectorAll('tr');
    rows.forEach(row => row.classList.remove('table-primary'));
    
    const selectedRow = Array.from(rows).find(row => 
        row.querySelector('.delete-btn').dataset.id == contact.id
    );
    
    if (selectedRow) {
        selectedRow.classList.add('table-primary');
    }
    

    document.getElementById('show-first-name').value = contact.firstName;
    document.getElementById('show-last-name').value = contact.lastName;
    document.getElementById('show-date-of-birth').value = contact.dateOfBirth || '';
    document.getElementById('show-address').value = contact.address || '';
    document.getElementById('show-email').value = contact.email || '';
    document.getElementById('show-phone').value = contact.phoneNumber || '';
    

    document.getElementById('edit-id').value = contact.id;
    document.getElementById('edit-first-name').value = contact.firstName;
    document.getElementById('edit-last-name').value = contact.lastName;
    document.getElementById('edit-date-of-birth').value = contact.dateOfBirth || '';
    document.getElementById('edit-address').value = contact.address || '';
    document.getElementById('edit-email').value = contact.email || '';
    document.getElementById('edit-phone').value = contact.phoneNumber || '';
    

    const showTabInstance = new bootstrap.Tab(showTab);
    showTabInstance.show();
}


async function handleEditContact(e) {
    e.preventDefault();
    
    const contactData = {
        id: document.getElementById('edit-id').value,
        firstName: document.getElementById('edit-first-name').value,
        lastName: document.getElementById('edit-last-name').value,
        phoneNumber: document.getElementById('edit-phone').value,
        email: document.getElementById('edit-email').value,
        address: document.getElementById('edit-address').value,
        dateOfBirth: document.getElementById('edit-date-of-birth').value || null
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

            loadContacts();
            alert('Contact updated successfully');
            

            const showTabInstance = new bootstrap.Tab(showTab);
            showTabInstance.show();
        } else {
            alert('Failed to update contact');
        }
    } catch (error) {
        console.error('Error updating contact:', error);
        alert('An error occurred while updating the contact');
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
            loadContacts();
            alert('Contact created successfully');
            

            newContactForm.reset();
            

            currentContactId = newContact.id;
        } else {
            alert('Failed to create contact');
        }
    } catch (error) {
        console.error('Error creating contact:', error);
        alert('An error occurred while creating the contact');
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

            loadContacts();
            alert('Contact deleted successfully');
            

            if (currentContactId === id) {
                clearForms();
                currentContactId = null;
            }
        } else {
            alert('Failed to delete contact');
        }
    } catch (error) {
        console.error('Error deleting contact:', error);
        alert('An error occurred while deleting the contact');
    }
}


function clearForms() {

    document.getElementById('show-first-name').value = '';
    document.getElementById('show-last-name').value = '';
    document.getElementById('show-date-of-birth').value = '';
    document.getElementById('show-address').value = '';
    document.getElementById('show-email').value = '';
    document.getElementById('show-phone').value = '';
    

    document.getElementById('edit-id').value = '';
    document.getElementById('edit-first-name').value = '';
    document.getElementById('edit-last-name').value = '';
    document.getElementById('edit-date-of-birth').value = '';
    document.getElementById('edit-address').value = '';
    document.getElementById('edit-email').value = '';
    document.getElementById('edit-phone').value = '';
}