package festivalmanager.hiring;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewShowForm {

	@NotEmpty   
	@NotNull
	@NotBlank
	private final String name;

	public NewShowForm(@NotNull String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
