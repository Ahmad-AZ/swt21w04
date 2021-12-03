package festivalmanager.hiring;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Artist {
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private Money price;
	@OneToMany
	private List<Show> shows;

	public Artist(@NotNull String name, @NotNull Money price) {
		this.name = name;
		this.price = price;
		this.shows = new ArrayList<>();
	}
	public Artist() {
		this.shows = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	public void addShow(Show show){
		shows.add(show);
	}
}
