package prototype.planning;

import org.springframework.data.util.Streamable;
import prototype.location.Location;
import prototype.festival.Festival;
import prototype.location.LocationManagement;
import prototype.location.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class PlanLocation extends Planning {

	private List<Location> locationList;
	public PlanLocation(Festival festival) {
		super(festival);
		locationList = new ArrayList<>();
	}

	public boolean bookLocation(Location location){
		if (!locationList.contains(location)){
			locationList.add(location);
			return true;
		}
		return false;
	}


//	public Streamable<Location> getAllLocations(){
//		return findAll();
//	}


}
