package prototype.planning;

import org.springframework.data.util.Streamable;
import prototype.location.Location;
import prototype.festival.Festival;
import prototype.location.LocationManagement;

import java.util.Set;

public class PlanLocation extends Planning {

	private LocationManagement locations;

	public PlanLocation(Festival festival) {
		super(festival);
	}

	public boolean bookLocation(Location location){

		return true;
	}

	public Streamable<Location> getAllLocations(){
		return locations.findAll();
	}

}
