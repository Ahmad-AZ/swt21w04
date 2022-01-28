package festivalmanager.location;



import java.util.Optional;

import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import festivalmanager.festival.Festival;

/**
 * Implementation of business logic related to {@link Location}s.
 *
 * @author Adrian Scholze
 */
@Service
@Transactional
public class LocationManagement { 
	
	private final LocationRepository locations;
	
	/**
	 * Creates a new {@link LocationManagement} with the given {@link LocationRepository}.
	 * 
	 * @param locations must not be {@literal null}.
	 */
	public LocationManagement(LocationRepository locations) {
		Assert.notNull(locations, "LoationRepository must not be null");
		this.locations = locations;		
	}
	
	/**
	 * Creates a new {@link Location} using the information given in the {@link NewLocationForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @return the new {@link Festival} instance.
	 */
	public Location createLocation(NewLocationForm form) {
		Assert.notNull(form, "NewLocationForm must not be null!");
		// save Festival in Repository
		 Money pricePerDay = Money.parse("EUR " + form.getPricePerDay());
		String image, groundView;
		// for unit tests
		if (form.getImageFile() == null) {
			image = "Blank_image";
		} else if (form.getImageFile().isEmpty()) {
			image = "Blank_image";
		} else {
			System.out.println("image setted");
			image = form.getImage();
		}
		// for unit tests
		if (form.getGroundViewFile() == null) {
			groundView = "Blank_groundview";
		} else if (form.getGroundViewFile().isEmpty()) {
			groundView = "Blank_groundview";
		} else {
			System.out.println("groundview setted");
			groundView = form.getGroundView();
		}	
		return locations.save(new Location(form.getName(), form.getAdress(), pricePerDay, form.getVisitorCapacity(), 
				form.getStageCapacity(), image, groundView));
	}
	
	/**
	 * Sets new attributes for given {@link Location} using the information given in the {@link NewLocationForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @param location must not be {@literal null}.
	 * @return the new {@link Festival} instance.
	 */
	public Location editLocation(Location location, NewLocationForm form) {
		Assert.notNull(form, "NewLocationForm must not be null!");
		Assert.notNull(location, "Location must not be null!");
		Money pricePerDay = Money.parse("EUR " + form.getPricePerDay());
		location.setPricePerDay(pricePerDay);
		location.setAdress(form.getAdress());
		location.setName(form.getName());
		location.setStageCapacity(form.getStageCapacity());
		location.setVisitorCapacity(form.getVisitorCapacity());
		if (form.getImageFile() != null && !(form.getImageFile().isEmpty())) {
			location.setImage(form.getImage());
		}
		if (form.getGroundViewFile() != null && !(form.getGroundViewFile().isEmpty())) {
			location.setGroundView(form.getGroundView());
		}
	
		return locations.save(location);
	}
	
	/**
	 * Saves {@link Location} instance
	 * 
	 * @param location must not be {@literal null}.
	 * @return the saved {@link Location} instance.
	 */
	public Location saveLocation(Location location) {
		Assert.notNull(location, "Location must not be null!");
		return locations.save(location);
	}
	
	/**
	 * Removes {@link Location} instance with given id 
	 * 
	 * @param locationId
	 */
	public void removeLocation(long locationId) {
		locations.deleteById(locationId);
	}

	/**
	 * Returns all {@link Locations}s currently available in the system.
	 * 
	 * @return all location entities
	 */ 
	public Streamable<Location> findAll(){
		return locations.findAll();
	}
	
	/**
	 * Returns {@link Location} with given id if it exists
	 * 
	 * @param id
	 * @return the {@link Location} with the given id or {@literal Optional#empty()} if none found.
	 */
	public Optional<Location> findById(Long id) {
		return locations.findById(id);
	}
}
