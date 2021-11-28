package festivalmanager.hiring;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class ArtistDataInitializer implements DataInitializer {

	private ArtistRepository artists;

	public ArtistDataInitializer(ArtistRepository artists){
		this.artists = artists;
		Artist jackson = new Artist("Michael Jackson");
		Artist shawn = new Artist("Shawn Mendes");
		Artist camila = new Artist("Camila Cabello");
		artists.save(jackson);
		artists.save(camila);
		artists.save(shawn);
	}
 
	@Override
	public void initialize() {
		//empty because initialization works in Constructor
	}
}
