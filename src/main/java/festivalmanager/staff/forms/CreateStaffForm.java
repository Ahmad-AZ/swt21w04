package festivalmanager.staff.forms;

import javax.validation.constraints.NotEmpty;

public class CreateStaffForm {
	@NotEmpty(message = "{CreateStaffForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{CreateStaffForm.password.NotEmpty}")
	private final String password;

	@NotEmpty(message = "{CreateStaffForm.role.NotEmpty}")
	private final String role;

	@NotEmpty(message = "{CreateStaffForm.salary.NotEmpty}")
	private final Double salary;

	public CreateStaffForm(String name, String password, String role, Double salary) {
		this.name = name;
		this.password = password;
		this.role = role;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public Double getSalary() {
		return salary;
	}
}
