package festivalmanager.hiring;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Artist {
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	// Temporarily added by Jan to get finances working, to be edited by Tuan
	private Money price;

	public Artist(@NotNull String name) {
		this.name = name;
		// Temporarily added by Jan to get finances working, to be edited by Tuan
		this.price = Money.of(11010.10, EURO);
	}
	public Artist() {}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Temporarily added by Jan to get finances working, to be edited by Tuan
	public Money getPrice() {
		return price;
	}
}
