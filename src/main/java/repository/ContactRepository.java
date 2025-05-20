package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import contactApp.Contact;

@Repository("ContactRepository")
public class ContactRepository implements IContactRepository {
    private static final String CONNECTION_STRING = "jdbc:sqlite:contactsdb.sqlite";

    // Constructor
    public ContactRepository() {
        createTables();
    }

    // Create table for data structure
    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "first_name TEXT NOT NULL,\n" +
                "last_name TEXT NOT NULL,\n" +
                "phone_number TEXT,\n" +
                "email_address TEXT,\n" +
                "address TEXT,\n" +
                "date_of_birth TEXT" +
                ");";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Get all contacts from the database and return as an List of Contact objects
    @Override
    public List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                contacts.add(new Contact(
                        result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("phone_number"),
                        result.getString("email_address"),
                        result.getString("address"),
                        result.getString("date_of_birth")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return contacts;
    }

    // Search a contact in database by id and retrieve an object of Contact
    public Optional<Contact> getContactById(Long id) {
        String sql = "SELECT * FROM contacts WHERE id = " + id;
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            if (result.next()) {
                Contact contact = new Contact();
                contact.setId(id);
                contact.setFirstName(result.getString("first_name"));
                contact.setLastName(result.getString("last_name"));
                contact.setPhoneNumber(result.getString("phone_number"));
                contact.setEmail(result.getString("email_address"));
                contact.setAddress(result.getString("address"));
                contact.setDateOfBirth(result.getString("date_of_birth"));
                return Optional.of(contact);
            } else {
                return Optional.empty();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    // Delete contact from database by given id
    public void deleteContact(Long id) {
        String sql = "DELETE FROM contacts WHERE id = " + id + ";";
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Add a contact to the database
    @Override
    public Contact addContact(Contact contact) {
        String sql = "INSERT INTO contacts (first_name, last_name, phone_number, email_address, address, date_of_birth)"
                +
                " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getPhoneNumber());
            stmt.setString(4, contact.getEmail());
            stmt.setString(5, contact.getAddress());
            stmt.setString(6, contact.getDateOfBirth());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long generatedId = generatedKeys.getInt(1);
                        contact.setId(generatedId); // Du brauchst setId(int id) in deiner Contact-Klasse
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return contact;
    }

    // Update contact info data when a contacts details have been changed
    @Override
    public Contact updateContact(Contact contact) {
        String sql = "UPDATE contacts SET first_name = ?, last_name = ?, phone_number = ?, email_address = ?, address = ?, date_of_birth = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getPhoneNumber());
            stmt.setString(4, contact.getEmail());
            stmt.setString(5, contact.getAddress());
            stmt.setString(6, contact.getDateOfBirth());
            stmt.setLong(7, contact.getId());

            stmt.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return contact;
    }
}