package prototype.location;

import com.mysema.commons.lang.Assert;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
	 * @return all location entittes
	 */
	public Streamable<Location> findAll(){
		return locations.findAll();
	}
	
	
}
