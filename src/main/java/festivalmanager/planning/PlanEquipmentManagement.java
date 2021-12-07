package festivalmanager.planning;
import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.Equipment.EquipmentType;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		
		Equipment equipment = equipmentManagement.findById(id).get();
		
	
		festival.setEquipments(id, amount);
		festivalManagement.saveFestival(festival);
		return true;
	}
	
	public boolean rentStage(String name, Equipment equipment, long festivalId) {
		
		Stage stage = new Stage(name, equipment.getRentalPerDay(), equipment.getLength(), equipment.getWidth());
		equipmentManagement.saveEquipment(stage);
		Festival festival = festivalManagement.findById(festivalId).get();
		festival.addStage(stage);
		festivalManagement.saveFestival(festival);
		
		return true;
	}
	
	public boolean unrentStage(Stage stage, Long festivalId) {
		Festival festival = festivalManagement.findById(festivalId).get();
	
		boolean success = festival.removeStage(stage);
		equipmentManagement.removeById(stage.getId());
		return success;
	
	}
	
//	public boolean rentStage(Stage stage, Festival festival) {
//		
//		festival.addStage(stage);
//		festivalManagement.saveFestival(festival);
//		
//		return true;
//	}
//	
//	public boolean unrent(Equipment equipment){
//		if (!equipments.contains(equipment)){
//			System.out.println("there is no such equipment to remove");
//			return false;
//		}
//		equipments.remove(equipment);
//		return true;
//	}
	
}
