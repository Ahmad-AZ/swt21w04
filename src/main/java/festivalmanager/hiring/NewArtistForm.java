package festivalmanager.hiring;

import org.javamoney.moneta.Money;

import javax.validation.constraints.NotNull;

public class NewArtistForm {

	private final String name;
	private final int price;

	public NewArtistForm(@NotNull String name, @NotNull int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
}
