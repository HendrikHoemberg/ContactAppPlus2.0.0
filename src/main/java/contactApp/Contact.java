package contactApp;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Contact entity class.
 */
@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	private String phoneNumber;

	@Email(message = "Please provide a valid email address")
	private String email;

	private String address;
	
	private String dateOfBirth;

	/**
	 * Default constructor.
	 */
	public Contact() {
	}

	/**
	 * Constructor.
	 * 
	 * @param id 
	 * @param firstName 
	 * @param lastName 
	 * @param phoneNumber 
	 * @param email 
	 * @param address
	 * @param dateOfBirth 
	 */
	public Contact(Long id, @NotBlank(message = "First name is required") String firstName,
			@NotBlank(message = "Last name is required") String lastName, String phoneNumber,
			@Email(message = "Please provide a valid email address") String email, String address, String dateOfBirth) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Compares this contact with another object for equality.
	 * Two contacts are considered equal if they have the same ID.
	 *
	 * @param o The object to compare with this contact
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contact contact = (Contact) o;
		return Objects.equals(id, contact.id);
	}

	/**
	 * Returns a hash code value for this contact based on its ID.
	 *
	 * @return A hash code value for this contact
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Returns a string representation of this contact.
	 * The string includes all the contact's fields.
	 *
	 * @return A string representation of this contact
	 */
	@Override
	public String toString() {
		return "Contact{" +
				"id=" + id +
				", fname='" + firstName + '\'' +
				", lname='" + lastName + '\'' +
				", birthdate=" + dateOfBirth +
				", address='" + address + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}