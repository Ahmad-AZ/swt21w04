package prototype.location;



import java.util.Optional;

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
