package festivalmanager.hiring;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NewArtistForm {

	private final String name;

	@Min(value = 0)
	@NotNull
	private final Double price;

	@Min(value = 0)
	@NotNull
	private final Integer stageTechnician;

	public NewArtistForm(@NotNull String name, @NotNull Double price, @NotNull Integer stageTechnician) {
		this.name = name;
		this.price = price;
		this.stageTechnician = stageTechnician;
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}

	public Integer getStageTechnician() {
		return stageTechnician;
	}
}
