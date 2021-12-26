package festivalmanager.Equipment;

import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EquipmentManagement {
	
	private final EquipmentRepository equipments;
	private final StageRepository stages;
	
	public EquipmentManagement(EquipmentRepository equipments, StageRepository stages) {
		this.equipments = equipments;
		this.stages = stages;
	}
		
	public Equipment saveEquipment(Equipment equipment) {
		return equipments.save(equipment);
	}
	
	public Stage saveStage(Stage stage) {
		return stages.save(stage);
	}
	
	public Optional<Equipment> findEquipmentById(SalespointIdentifier id) {
		System.out.println("find id " + id);
		return equipments.findById(id);
	}
	
	public Optional<Stage> findStageById(SalespointIdentifier id) {
		return stages.findById(id);
	}
	
	public void removeEquipmentById(SalespointIdentifier id) {
		equipments.deleteById(id);
	}
	
	public void removeStageById(SalespointIdentifier id) {
		stages.deleteById(id);
	}
	
	/**
	 *
	 * @return all equipment entities
	 */ 
	public Streamable<Equipment> findlAllEquipments(){
		return equipments.findAll();
	}
	
	public Streamable<Stage> findlAllStages(){
		return stages.findAll();
	}
}
