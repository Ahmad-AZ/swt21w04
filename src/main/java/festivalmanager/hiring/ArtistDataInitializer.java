package festivalmanager.hiring;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Initializes default artists and their shows
 *
 * @author Tuan Giang Trinh
 */
@Component
@Order(20)
public class ArtistDataInitializer implements DataInitializer {
 
	private ArtistRepository artists;

	/**
	 * Create a new {@link ArtistDataInitializer} and add new {@link Artist} to {@link ArtistRepository}
	 * @param artists
	 */
	public ArtistDataInitializer(ArtistRepository artists){
		this.artists = artists;
		
	}
 
	@Override
	public void initialize() {
		if(artists.findAll().iterator().hasNext()) {
			return;
		}
		Artist jackson = new Artist("Michael Jackson", Money.of(99.9, EURO), 4);
		Artist shawn = new Artist("Shawn Mendes", Money.of(74.9, EURO), 7);
		Artist camila = new Artist("Camila Cabello", Money.of(83, EURO), 5);
		jackson.addBooking(LocalDate.of(2021, 12, 23), LocalDate.of(2022, 1,1));

		Show criminal = new Show("Smooth criminal", Duration.ofMinutes(49));
		Show bad = new Show("bad", Duration.ofMinutes(80));
		Show senorita = new Show("senorita", Duration.ofMinutes(145));
		Show treat = new Show("treat you better", Duration.ofMinutes(60));
		Show havana = new Show("havana", Duration.ofMinutes(75));
		Show never = new Show("never be the same", Duration.ofMinutes(90));
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
}
