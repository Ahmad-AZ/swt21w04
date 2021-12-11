package festivalmanager.hiring;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
public class ArtistDataInitializer implements DataInitializer {
 
	private ArtistRepository artists;

	public ArtistDataInitializer(ArtistRepository artists){
		this.artists = artists;
		Artist jackson = new Artist("Michael Jackson", Money.of(99.9, EURO), 4);
		Artist shawn = new Artist("Shawn Mendes", Money.of(74.9, EURO), 7);
		Artist camila = new Artist("Camila Cabello", Money.of(83, EURO), 5);
		jackson.addBooking(LocalDate.of(2021, 12, 23), LocalDate.of(2022, 1,1));

		Show criminal = new Show("Smooth criminal");
		Show bad = new Show("bad");
		Show senorita = new Show("senorita");
		Show treat = new Show("treat you better");
		Show havana = new Show("havana");
		Show never = new Show("never be the same");
		jackson.addShow(criminal);
		jackson.addShow(bad);
		shawn.addShow(senorita);
		shawn.addShow(treat);
		camila.addShow(havana);
		camila.addShow(never);

		artists.save(jackson);
		artists.save(camila);
		artists.save(shawn);
	}
 
	@Override
	public void initialize() {
		//empty because initialization works in Constructor
	}
}
