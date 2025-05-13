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

    public ContactRepository() {
        createTables();
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "first_name TEXT NOT NULL,\n" +
                "last_name TEXT NOT NULL,\n" +
                "phone_number TEXT\n" +
                "email_address TEXT,\n" +
                "address TEXT,\n" +
                "date_of_birth TEXT,\n" +
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
                        result.getLong("coid"),
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
                contact.setDateOfBirth(result.getString("date_of_birth"));
                contact.setAddress(result.getString("address"));
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
        String sql = "INSERT INTO contacts (first_name, last_name, phone_number, email_address, birth_date, " +
                "group_id, street, house_number, zip_code, city, state, country) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getPhoneNumber());
            stmt.setString(4, contact.getEmail());
            stmt.setDate(5, contact.getBirthdate()); // java.sql.Date
            stmt.setInt(6, contact.getgroupId());
            stmt.setString(7, contact.getstreet());
            stmt.setString(8, contact.gethouseNumber());
            stmt.setString(9, contact.getzipCode());
            stmt.setString(10, contact.getcity());
            stmt.setString(11, contact.getstate());
            stmt.setString(12, contact.getcountry());

            stmt.executeUpdate();
        } catch (Exception exception) {

        }
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