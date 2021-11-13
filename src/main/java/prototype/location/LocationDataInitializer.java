//package prototype.location;
//import static org.salespointframework.core.Currencies.EURO;
//
//import org.javamoney.moneta.Money;
//import org.salespointframework.core.DataInitializer;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//// import prototype.location.Location;
//
//
//@Component
//@Order(20)
//public class LocationDataInitializer implements DataInitializer{
//	
//
//	
//	private LocationCatalog locationCatalog;
//	
//	public LocationDataInitializer(LocationCatalog locationCatalog) {
//		this.locationCatalog = locationCatalog;
//		Location l1 = new Location("Homeee", "athome", Money.of(100, EURO), 12, 1);
//		locationCatalog.save(l1);
//	}
//	
//	@Override 
//	public void initialize() {
//
////		if (LocationManagement.findAll().iterator().hasNext()) {
////			return; 
////		}
//
//		//LOG.info("Creating default catalog entries.");
//
//		
//	}
//
//}
