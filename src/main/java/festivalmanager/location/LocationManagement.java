package festivalmanager.location;



import java.util.Optional;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


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
		return locations.save(new Location(form.getName(), form.getAdress(), pricePerDay, form.getVisitorCapacity(), 
				form.getStageCapacity(), null, null));
	}
	
	public Location editLocation(Location location, NewLocationForm form) {
		Money pricePerDay = Money.of(form.getPricePerDay(), EURO);
		location.setPricePerDay(pricePerDay);
		location.setAdress(form.getAdress());
		location.setName(form.getName());
		location.setStageCapacity(form.getStageCapacity());
		location.setVisitorCapacity(form.getVisitorCapacity());
		return locations.save(location);
	}
	
	public Location saveLocation(Location location) {
		return locations.save(location);
	}
	
	public void removeLocation(long locationId) {
		// id == 0 throws Error
		locations.deleteById(locationId);
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
