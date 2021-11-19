package prototype.staff;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@OneToOne
	private UserAccount account;

	public Person() {}

	public Person(String name, UserAccount account) {
		this.name = name;
		this.account = account;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public UserAccount getAccount() {
		return account;
	}
}
