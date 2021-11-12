package prototype.location;

import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

// import prototype.prototype.location.Location;


@Component
public class LocationDataInitializer implements DataInitializer{
	
	private LocationManagement locationManagement;
	
	private LocationCatalog locationCatalog;
	
	public LocationDataInitializer(LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
	}
	
	@Override
	public void initialize() {

//		if (LocationManagement.findAll().iterator().hasNext()) {
//			return;
//		}

		//LOG.info("Creating default catalog entries.");

		locationCatalog.save(new Location("Homeee", "athome", Money.of(100, EURO), 12, 1));
	}

}
