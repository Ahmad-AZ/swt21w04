package festivalmanager.hiring;

import org.javamoney.moneta.Money;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NewArtistForm {

	private final String name;

	@Min(value = 0)
	@NotNull
	private final Double price;

	public NewArtistForm(@NotNull String name, @NotNull Double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
}
