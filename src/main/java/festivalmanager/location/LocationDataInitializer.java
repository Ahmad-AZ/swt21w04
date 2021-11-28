package festivalmanager.location;

import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDate;
//import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class LocationDataInitializer implements DataInitializer {

	private LocationRepository locations;

	public LocationDataInitializer(LocationRepository locations) {
		this.locations = locations;
		Location l1 = new Location("Kulturpalast", "Dresden", Money.of(1550, EURO), 1500, 12, "Kulturpalast_image",
				"Kulturpalast_groundview");
		l1.addBooking(LocalDate.of(2021, 11, 23), LocalDate.of(2021, 12, 23));
		locations.save(l1);
		Location l2 = new Location("Hörsaalzentrum", "Dresden", Money.of(1828, EURO), 1828, 6, "HSZ_image",
				"HSZ_groundview");
		locations.save(l2);
		Location l3 = new Location("Rundkino", "Dresden", Money.of(1760, EURO), 1500, 8, "Rundkino_image",
				"Rundkino_groundview");
		locations.save(l3);
	}

	@Override
	public void initialize() {

		// if (LocationManagement.findAll().iterator().hasNext()) {
		// return;
		// }

		// LOG.info("Creating default catalog entries.");

	}

	protected LocationRepository getLocationRepository() {
		return locations;
	}

}
