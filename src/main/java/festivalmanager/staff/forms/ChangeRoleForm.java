package festivalmanager.staff.forms;

import javax.validation.constraints.NotEmpty;

public class ChangeRoleForm {
	@NotEmpty(message = "{ChangeNameForm.role.NotEmpty}")
	private final String role;

	@NotEmpty(message = "{ChangeNameForm.id.NotEmpty}")
	private final long id;

	public ChangeRoleForm(String role, long id) {
		this.role = role;
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public long getId() {
		return id;
	}
}
