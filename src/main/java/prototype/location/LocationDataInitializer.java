package prototype.location;
import static org.salespointframework.core.Currencies.EURO;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@Component
@Order(20)
public class LocationDataInitializer implements DataInitializer{
	
	private LocationRepository locations;
	
	public LocationDataInitializer(LocationRepository locations) {
		this.locations = locations;
		Location l1 = new Location("Kulturpalast", "Dresden", Money.of(1550, EURO), 1500, 12);
		locations.save(l1);
		Location l2 = new Location("Hörsaalzentrum", "Dresden", Money.of(1828, EURO), 1828, 6);
		locations.save(l2);
		Location l3 = new Location("Rundkino", "Dresden", Money.of(1760, EURO), 1500, 8);
		locations.save(l3);
	}
	
	@Override 
	public void initialize() {

//		if (LocationManagement.findAll().iterator().hasNext()) {
//			return; 
//		}

		//LOG.info("Creating default catalog entries.");

		
	}

}
