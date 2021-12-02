package festivalmanager.hiring;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
public class ArtistDataInitializer implements DataInitializer {

	private ArtistRepository artists;

	public ArtistDataInitializer(ArtistRepository artists){
		this.artists = artists;
		Artist jackson = new Artist("Michael Jackson", Money.of(99.9, EURO));
		Artist shawn = new Artist("Shawn Mendes", Money.of(74.9, EURO));
		Artist camila = new Artist("Camila Cabello", Money.of(83, EURO));
		artists.save(jackson);
		artists.save(camila);
		artists.save(shawn);
	}
 
	@Override
	public void initialize() {
		//empty because initialization works in Constructor
	}
}
