package festivalmanager.hiring;

import javax.validation.constraints.NotNull;

public class NewArtistForm {

	private final String name;

	public NewArtistForm(@NotNull String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
