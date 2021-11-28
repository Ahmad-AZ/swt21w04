package festivalmanager.staff.forms;

import javax.validation.constraints.NotEmpty;

public class ChangePasswordForm {
	@NotEmpty(message = "{ChangePasswordForm.password.NotEmpty}")
	private final String password;

	@NotEmpty(message = "{ChangePasswordForm.id.NotEmpty}")
	private final long id;

	public ChangePasswordForm(String password, long id) {
		this.password = password;
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public long getId() {
		return id;
	}
}
