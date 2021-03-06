package festivalmanager.staff.forms;

import javax.validation.constraints.NotEmpty;

public class RemoveStaffForm {
	@NotEmpty(message = "{RemoveStaffForm.id.NotEmpty}")
	Long id;

	public RemoveStaffForm(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
