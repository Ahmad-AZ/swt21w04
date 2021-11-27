package festivalmanager.staff;

import javax.validation.constraints.NotEmpty;

public class CreateStaffForm {
	@NotEmpty(message = "{CreateStaffForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{CreateStaffForm.password.NotEmpty}")
	private final String password;

	@NotEmpty(message = "{CreateStaffForm.role.NotEmpty}")
	private final String role;

	public CreateStaffForm(String name, String password, String role) {
		this.name = name;
		this.password = password;
		this.role = role;
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
}
