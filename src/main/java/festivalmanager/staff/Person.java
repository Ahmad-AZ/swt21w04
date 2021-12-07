package festivalmanager.staff;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long festivalId;
	private String name;

	private String role;

	private double salary;

	@OneToOne
	private UserAccount userAccount;

	public Person() {}

	public Person(long festivalId, String name, String role, double salary, UserAccount userAccount) {
		this.festivalId = festivalId;
		this.name = name;
		this.role = role;
		this.salary = salary;
		this.userAccount = userAccount;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getSalary() {
		return salary;
	}
	public UserAccount getUserAccount() {
		return userAccount;
	}
}
