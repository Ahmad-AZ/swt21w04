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
	private LocationManagement locationManagement;
	private FestivalManagement festivalManagement;
	
	public PlanLocationManagement(FestivalManagement festivalManagement, LocationManagement locationManagement) {
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
		locationList = locationManagement.findAll();
	}

	public boolean bookLocation(Location location, Festival festival){
		boolean succes = location.addBooking(festival.getStartDate(), festival.getEndDate());
		if(succes) {
			locationManagement.saveLocation(location);
			festival.setLocation(location);
			festivalManagement.saveFestival(festival);
			return true;
		}
		return false;
	}


//	public Streamable<Location> getAllLocations(){
//		return findAll();
//	}


}
