package festivalmanager.staff;

import festivalmanager.staff.forms.CreateStaffForm;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Objects;

/**
 * class that represents a person
 * @author Georg Kunze
 */
@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long festivalId;
	private String name;

	private String role;

	private Money salary;

	@OneToOne
	private UserAccount userAccount;

	/**
	 * default constructor
	 */
	public Person() {}

	/**
	 * constructor for initializing the person instance
	 * @param festivalId			the id of the festival, the person should be associated with
	 * @param form					the {@link CreateStaffForm} containing all required data to create a person
	 * @param userAccount			the {@link UserAccount} of the person
	 */
	public Person(long festivalId, CreateStaffForm form, UserAccount userAccount) {
		this.festivalId = festivalId;
		this.name = form.getName();
		this.role = form.getRole();
		this.salary = Money.of(form.getSalary(), EURO);
		this.userAccount = userAccount;
	}

	/**
	 * getter for the id field
	 * @return					the id of this person
	 */
	public long getId() {
		return id;
	}

	/**
	 * getter for the festivalId field
	 * @return					the id of the festival of this person
	 */
	public long getFestivalId() {
		return festivalId;
	}

	/**
	 * getter for the name field
	 * @return					the name of this person
	 */
	public String getName() {
		return name;
	}

	/**
	 * getter for the role field
	 * @return					the role of this person
	 */
	public String getRole() {
		return role;
	}

	/**
	 * setter for the role field
	 * @param role				the new role, the person should have
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * getter for the salary field
	 * @return					the salary of this person
	 */
	public Money getSalary() {
		return salary;
	}

	/**
	 * setter for the salary field
	 * @param salary			the new salary of this person
	 */
	public void setSalary(Double salary) {
		this.salary = Money.of(salary, EURO);
	}

	/**
	 * getter for the userAccount field
	 * @return					the userAccount of this person
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * function to create a unique hash of this person
	 * @return					the hash
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * function to check whether to instances of {@link Person} are equal
	 * @param obj				the object to compare this instance to
	 * @return					a boolean whether both instances are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Person other = (Person) obj;
		return id == other.id;
	}
}
