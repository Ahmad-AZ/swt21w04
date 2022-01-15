package festivalmanager.planning;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import festivalmanager.Equipment.Equipment;
import festivalmanager.Equipment.EquipmentManagement;
import festivalmanager.Equipment.Stage;
import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;


/**
 * Implementation of business logic related to {@link Equipment}, {@link Stage} and {@link Festival}
 *
 * @author Adrian Scholze
 */
@Service
@Transactional
public class PlanEquipmentManagement {
	
	private final FestivalManagement festivalManagement;
	private final EquipmentManagement equipmentManagement;

	/**
	 * Create a new {@link PlanEquipmentManagement}
	 * @param equipmentManagement
	 * @param festivalManagement
	 */
	public PlanEquipmentManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
	}
	
	/**
	 * rent the {@link Equipment} for the {@link Festival}
	 * @param id
	 * @param amount
	 * @param festival
	 */
	public void rentEquipment(SalespointIdentifier id, long amount, Festival festival){
		festival.setEquipments(id, amount);
		festivalManagement.saveFestival(festival);
	}
	
	/**
	 * rent the {@link Stage} for the {@link Festival}
	 * @param name
	 * @param equipment
	 * @param festival
	 */
	public void rentStage(String name, Equipment equipment, Festival festival) {
		Stage stage = new Stage(name, equipment.getRentalPerDay());
		equipmentManagement.saveStage(stage);
		festival.addStage(stage);
		festivalManagement.saveFestival(festival);
	}
	
	/**
	 * unrent the {@link Stage} for the {@link Festival}
	 * @param stage
	 * @param festival
	 * @return true if unrent stage success
	 */
	public boolean unrentStage(Stage stage, Festival festival) {
		boolean success = festival.removeStage(stage);
		festivalManagement.saveFestival(festival);
		equipmentManagement.removeStageById(stage.getId());

		return success;
	
	}
	

	
}
