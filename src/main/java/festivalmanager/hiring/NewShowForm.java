package festivalmanager.hiring;

import javax.validation.constraints.NotNull;

public class NewShowForm {

	private final String name;

	public NewShowForm(@NotNull String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
