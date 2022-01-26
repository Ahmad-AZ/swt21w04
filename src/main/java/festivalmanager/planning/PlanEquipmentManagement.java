package festivalmanager.planning;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
	 * 
	 * @param equipmentManagement must not be {@literal null}.
	 * @param festivalManagement must not be {@literal null}.
	 */
	public PlanEquipmentManagement(FestivalManagement festivalManagement, EquipmentManagement equipmentManagement) {
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		Assert.notNull(equipmentManagement, "EquipmentManagement must not be null!");
		this.festivalManagement = festivalManagement;
		this.equipmentManagement = equipmentManagement;
	}
	
	/**
	 * Rent an amount of an {@link Equipment} for a {@link Festival}
	 * 
	 * @param id
	 * @param amount
	 * @param festival
	 */
	public void rentEquipment(SalespointIdentifier id, long amount, Festival festival){
		festival.setEquipments(id, amount);
		festivalManagement.saveFestival(festival);
	}
	
	/**
	 * Rent a {@link Stage} for a {@link Festival}
	 * 
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
	 * Unrent a {@link Stage} for a {@link Festival}
	 * 
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
