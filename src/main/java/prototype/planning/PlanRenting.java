package prototype.planning;

import prototype.Equipment.Equipment;
import prototype.festival.Festival;

import java.util.ArrayList;
import java.util.List;

public class PlanRenting extends Planning {

	private int numberOfStage;
	private List<Equipment> equipments;

	/**
	 *
	 * @param festival
	 */
	PlanRenting(Festival festival) {

		super(festival);
		equipments=new ArrayList<>();
	}

	public void rent(Equipment equipment){
		equipments.add(equipment);
	}

	public boolean unrent(Equipment equipment){
		if (!equipments.contains(equipment)){
			System.out.println("there is no such equipment to remove");
			return false;
		}
		equipments.remove(equipment);
		return true;
	}


}
