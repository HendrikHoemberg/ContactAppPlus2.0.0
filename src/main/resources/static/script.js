const contactTableBody = document.getElementById('contactTableBody');
const showContactForm = document.getElementById('showContactForm');
const editContactForm = document.getElementById('editContactForm');
const newContactForm = document.getElementById('newContactForm');


const showTab = document.getElementById('show-tab');
const editTab = document.getElementById('edit-tab');
const newTab = document.getElementById('new-tab');


let currentContactId = null;


document.addEventListener('DOMContentLoaded', () => {
    loadContacts();
    

    editContactForm.addEventListener('submit', handleEditContact);
    newContactForm.addEventListener('submit', handleNewContact);
});


async function loadContacts() {
    try {
        const response = await fetch('/api/contacts');
        const contacts = await response.json();
        
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
            <td>${contact.fname}</td>
            <td>${contact.lname}</td>
            <td>${contact.email || ''}</td>
            <td>${contact.phoneNumber || ''}</td>
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
    

    document.getElementById('show-fname').value = contact.fname;
    document.getElementById('show-lname').value = contact.lname;
    document.getElementById('show-date-of-birth').value = contact.date-of-birth || '';
    document.getElementById('show-address').value = contact.address || '';
    document.getElementById('show-email').value = contact.email || '';
    document.getElementById('show-phone').value = contact.phoneNumber || '';
    

    document.getElementById('edit-id').value = contact.id;
    document.getElementById('edit-fname').value = contact.fname;
    document.getElementById('edit-lname').value = contact.lname;
    document.getElementById('edit-date-of-birth').value = contact.date-of-birth || '';
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
        fname: document.getElementById('edit-fname').value,
        lname: document.getElementById('edit-lname').value,
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
        fname: document.getElementById('new-fname').value,
        lname: document.getElementById('new-lname').value,
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

    document.getElementById('show-fname').value = '';
    document.getElementById('show-lname').value = '';
    document.getElementById('show-date-of-birth').value = '';
    document.getElementById('show-address').value = '';
    document.getElementById('show-email').value = '';
    document.getElementById('show-phone').value = '';
    

    document.getElementById('edit-id').value = '';
    document.getElementById('edit-fname').value = '';
    document.getElementById('edit-lname').value = '';
    document.getElementById('edit-date-of-birth').value = '';
    document.getElementById('edit-address').value = '';
    document.getElementById('edit-email').value = '';
    document.getElementById('edit-phone').value = '';
}