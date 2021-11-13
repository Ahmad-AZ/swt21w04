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
		Location l1 = new Location("Homeee", "athome", null, 12, 1);
		locations.save(l1);
	}
	
	@Override 
	public void initialize() {

//		if (LocationManagement.findAll().iterator().hasNext()) {
//			return; 
//		}

		//LOG.info("Creating default catalog entries.");

		
	}

}
