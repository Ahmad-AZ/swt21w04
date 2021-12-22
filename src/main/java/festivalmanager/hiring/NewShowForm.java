package festivalmanager.hiring;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewShowForm {

	@NotEmpty   
	@NotNull
	@NotBlank
	private final String name;

	@Min(value = 0)
	@NotNull
	private final long performance;
	public NewShowForm(@NotNull String name, @NotNull @NotEmpty @NotBlank Integer performance) {
		this.name = name;
		this.performance = performance;
	}

	public String getName() {
		return name;
	}

	public long getPerformance() {
		return performance;
	}
}
