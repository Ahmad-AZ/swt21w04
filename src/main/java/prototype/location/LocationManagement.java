package prototype.location;



import java.util.Optional;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import prototype.festival.Festival;


@Service
@Transactional
public class LocationManagement { 
	
	private final LocationRepository locations;

	/**
	 * @param locations
	 */
	public LocationManagement(LocationRepository locations) {
		Assert.notNull(locations, "LoationRepository must not be null");
		this.locations = locations;
	}
	
	public Location createLocation(NewLocationForm form) {
		// save Festival in Repository
		Money pricePerDay = Money.of(form.getPricePerDay(), EURO);
		return locations.save(new Location(form.getName(), form.getAdress(), pricePerDay, form.getVisitorCapacity(), form.getStageCapacity()));
	}
	
	public Location saveLocation(Location location) {
		return locations.save(location);
	}

	/**
	 *
	 * @return all location entities
	 */ 
	public Streamable<Location> findAll(){
		return locations.findAll();
	}
	
	public Optional<Location> findById(Long id) {
		return locations.findById(id);
	}
}
