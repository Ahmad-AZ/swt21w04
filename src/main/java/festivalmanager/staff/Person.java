package festivalmanager.staff;

import festivalmanager.staff.forms.CreateStaffForm;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

import static org.salespointframework.core.Currencies.EURO;

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

	public Person() {}

	public Person(long festivalId, CreateStaffForm form, UserAccount userAccount) {
		this.festivalId = festivalId;
		this.name = form.getName();
		this.role = form.getRole();
		this.salary = Money.of(form.getSalary(), EURO);
		this.userAccount = userAccount;
	}

	public long getId() {
		return id;
	}

	public long getFestivalId() {
		return festivalId;
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

	public Money getSalary() {
		return salary;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
}
