package festivalmanager.staff.forms;

import javax.validation.constraints.NotEmpty;

public class ChangeSalaryForm {
	@NotEmpty(message = "{ChangeSalaryForm.salary.NotEmpty}")
	private final Double salary;

	@NotEmpty(message = "{ChangeSalaryForm.id.NotEmpty}")
	private final long id;

	public ChangeSalaryForm(Double salary, long id) {
		this.salary = salary;
		this.id = id;
	}

	public Double getSalary() {
		return salary;
	}

	public long getId() {
		return id;
	}
}
