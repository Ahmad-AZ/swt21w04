package festivalmanager.planning;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.location.Location;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.LocationManagement;

/**
 * Implementation of business logic related to {@link Location}and {@link Festival}
 *
 * @author Adrian Scholze
 */
@Service
@Transactional
public class PlanLocationManagement {

	private final LocationManagement locationManagement;
	private final FestivalManagement festivalManagement;
	
	/**
	 * Create a new {@link PlanLocationManagement}
	 * @param locationManagement
	 * @param festivalManagement
	 */
	public PlanLocationManagement(FestivalManagement festivalManagement, LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
	}

	/**
	 * book the {@link Location} for the {@link Festival}
	 * @param location
	 * @param festival
	 * @return true if book location success
	 */
	public boolean bookLocation(Location location, Festival festival){
		location.removeBooking(festival.getStartDate(), festival.getEndDate());
		boolean succes = location.addBooking(festival.getStartDate(), festival.getEndDate());
		locationManagement.saveLocation(location);
		if(succes) {
			
			festival.setLocation(location);
			festivalManagement.saveFestival(festival);
			return true;
		}
		return false;
	}
	
	/**
	 * unbook the {@link Location} for the {@link Festival}
	 * @param location
	 * @param festival
	 * @return true if unbook location success
	 */
	public void unbookLocation(Location location, Festival festival) {
		festival.setLocation(null);
		festivalManagement.saveFestival(festival);
		location.removeBooking(festival.getStartDate(), festival.getEndDate());
		locationManagement.saveLocation(location);
	}


}
