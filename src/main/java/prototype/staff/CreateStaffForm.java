package prototype.staff;

import javax.validation.constraints.NotEmpty;

public class CreateStaffForm {
	@NotEmpty(message = "{CreateStaffForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{CreateStaffForm.password.NotEmpty}")
	private final String password;

	public CreateStaffForm(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
}
