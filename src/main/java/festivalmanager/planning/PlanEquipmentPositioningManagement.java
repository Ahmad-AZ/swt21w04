package festivalmanager.planning;
import festivalmanager.Equipment.Equipment;
import festivalmanager.festival.Festival;

import java.util.ArrayList;
import java.util.List;

public class PlanEquipmentPositioningManagement {

	private int numberOfStage;
	private List<Equipment> equipments;

	public PlanEquipmentPositioningManagement(Festival festival) {
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
