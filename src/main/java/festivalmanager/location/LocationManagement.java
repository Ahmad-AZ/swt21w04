package festivalmanager.location;



import java.io.InputStream;
import java.util.Optional;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
	
	public Location editLocation(Location location, NewLocationForm form) {
		Money pricePerDay = Money.of(form.getPricePerDay(), EURO);
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
