package repository;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public ContactRepository() {
        createTables();
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "fname TEXT NOT NULL,\n" +
                "lname TEXT NOT NULL,\n" +
                "birthdate TEXT,\n" +
                "address TEXT,\n" +
                "email TEXT,\n" +
                "phone_number TEXT\n" +
                ");";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                contacts.add(new Contact(
                        result.getLong("contact_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("phone_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public Optional<Contact> getContactById(Long id) {
        String sql = "SELECT * FROM contacts WHERE contact_id = " + id;
        Contact contact = new Contact();
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            if (result.next()) {
                contact.setId(id);
                contact.setFirstName(result.getString("first_name"));
                contact.setLastName(result.getString("last_name"));
                contact.setPhoneNumber(result.getString("phone_number"));
                contact.setEmail(result.getString("email_address"));
                contact.setBirthdate(result.getString("birth_date"));
                contact.setStreet(result.getString("street"));
                contact.setHouseNumber(result.getString("house_number"));
                contact.setZip(result.getString("zip_code"));
                contact.setCity(result.getString("city"));
                contact.setState(result.getString("state"));
                contact.setCountry(result.getString("country"));
            }
        } catch (Exception exception) {

        }
        return null;
    }

    public void deleteContact(Long id) {
        String sql = "";
    }

    @Override
    public Contact addContact(Contact contact) {
        String sql = "";
        // TO-DO
        throw new UnsupportedOperationException("Unimplemented method 'addContact'");
    }

    @Override
    public Contact updateContact(Contact contact) {
        String sql = "";
        // TO-DO
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }
}