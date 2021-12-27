package festivalmanager.planning;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class PlanEquipmentManagement {
	
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;

	public PlanEquipmentManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
	}

	public void rentEquipment(SalespointIdentifier id, long amount, Festival festival){
		festival.setEquipments(id, amount);
		festivalManagement.saveFestival(festival);
	}
	
	public void rentStage(String name, Equipment equipment, Festival festival) {

		Stage stage = new Stage(name, equipment.getRentalPerDay());
		equipmentManagement.saveStage(stage);
		festival.addStage(stage);
		festivalManagement.saveFestival(festival);
	}
	
	public boolean unrentStage(Stage stage, Long festivalId) {
		Festival festival = festivalManagement.findById(festivalId).get();	

		boolean success = festival.removeStage(stage);
		festivalManagement.saveFestival(festival);
		equipmentManagement.removeStageById(stage.getId());

		return success;
	
	}
	

	
}
