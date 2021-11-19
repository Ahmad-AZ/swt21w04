package prototype.planning;

import org.springframework.data.util.Streamable;
import prototype.location.Location;
import prototype.festival.Festival;
import prototype.festival.FestivalManagement;
import prototype.location.LocationManagement;
import prototype.location.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class PlanLocation extends Planning {

	private Streamable<Location> locationList;
	private LocationManagement locationManagement;
	private FestivalManagement festivalManagement;
	
	public PlanLocation(Festival festival, FestivalManagement festivalManagement, LocationManagement locationManagement) {
		super(festival);
		this.locationManagement = locationManagement;
		this.festivalManagement = festivalManagement;
		locationList = locationManagement.findAll();
	}

	public boolean bookLocation(Location location){
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
