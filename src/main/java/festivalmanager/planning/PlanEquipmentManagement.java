package festivalmanager.planning;
import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Equipments;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class PlanEquipmentManagement {
	
	private long currentMaxNumberOfStage;
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;

	public PlanEquipmentManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
	}

	public boolean rentEquipment(long id, long amount, Festival festival){
		currentMaxNumberOfStage= festival.getLocation().getStageCapacity();
		
		if(amount<= festival.getLocation().getStageCapacity()) {
			festival.setEquipments(id, amount);
			System.out.println("works");
			festivalManagement.saveFestival(festival);
			return true;
		}
		else {
			return false;
		}
	}
	
//	public boolean unrent(Equipment equipment){
//		if (!equipments.contains(equipment)){
//			System.out.println("there is no such equipment to remove");
//			return false;
//		}
//		equipments.remove(equipment);
//		return true;
//	}
	
}
