package festivalmanager.hiring;

import javax.validation.constraints.NotNull;

public class newShowForm {

	private final String name;

	public newShowForm(@NotNull String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
