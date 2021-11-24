package prototype.planning;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prototype.location.Location;
import prototype.festival.Festival;
import prototype.festival.FestivalManagement;
import prototype.location.LocationManagement;
import prototype.location.LocationRepository;

import java.util.ArrayList;
import java.util.List;

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
