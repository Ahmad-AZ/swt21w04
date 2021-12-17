package festivalmanager.planning;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.festival.Schedule;

import java.util.ArrayList;
import java.util.List;

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

	public boolean rentEquipment(long id, long amount, Festival festival){
		
		Equipment equipment = equipmentManagement.findById(id).get();
		
	
		festival.setEquipments(id, amount);
		festivalManagement.saveFestival(festival);
		return true;
	}
	
	public boolean rentStage(String name, Equipment equipment, Festival festival) {

		Stage stage = new Stage(name, equipment.getRentalPerDay(), equipment.getLength(), equipment.getWidth());
		equipmentManagement.saveEquipment(stage);
		festival.addStage(stage);
		festivalManagement.saveFestival(festival);

		return true;
	}
	
	public boolean unrentStage(Stage stage, Long festivalId) {
		Festival festival = festivalManagement.findById(festivalId).get();
		
		// get all Schedules to remove at this stage
		List<Schedule> deleteList = new ArrayList<>();
		for(Schedule aSchedule : festival.getSchedules()) {
			if(aSchedule.getStage().equals(stage)) {
				deleteList.add(aSchedule);
			}
		}
		
		//required to avoid delete error
		for(Schedule aSchedule : deleteList) {
			festival.removeSchedule(aSchedule.getTimeSlot(), stage, aSchedule.getDate());	
		}
		
		
		System.out.println("before");
		boolean success = festival.removeStage(stage);
		System.out.println("middle "+ festival.getStages() + " "+ festival.getSchedules());
		festivalManagement.saveFestival(festival);
		
		System.out.println("after "+ success);
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
