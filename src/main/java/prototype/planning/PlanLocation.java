package prototype.planning;

import prototype.location.Location;
import prototype.festival.Festival;

import java.util.Set;

public class PlanLocation extends Planning {

	public PlanLocation(Festival festival) {
		super(festival);
	}

	public boolean bookLocation(Location location){
		return true;
	}
	public Set <Location> getAllLocations(){
		return null;
		//return HashSet <Location>;
	}

}
