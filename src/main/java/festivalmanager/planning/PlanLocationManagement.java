package festivalmanager.planning;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.location.Location;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.LocationManagement;

@Service
@Transactional
public class PlanLocationManagement {

	private Streamable<Location> locationList;
	private final LocationManagement locationManagement;
	private final FestivalManagement festivalManagement;
	
	public PlanLocationManagement(FestivalManagement festivalManagement, LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
		locationList = locationManagement.findAll();
	}

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
	
	public void unbookLocation(Location location, Festival festival) {
		festival.setLocation(null);
		festivalManagement.saveFestival(festival);
		location.removeBooking(festival.getStartDate(), festival.getEndDate());
		locationManagement.saveLocation(location);
	}


//	public Streamable<Location> getAllLocations(){
//		return findAll();
//	}


}
