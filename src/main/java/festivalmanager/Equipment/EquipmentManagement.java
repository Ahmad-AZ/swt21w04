package festivalmanager.Equipment;

import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of business logic related to {@link Equipment}
 *
 * @author Tuan Giang Trinh
 */
@Service
@Transactional
public class EquipmentManagement {
	
	private final EquipmentRepository equipments;
	private final StageRepository stages;
	/**
	 * Create a new {@link EquipmentManagement} with the given {@link EquipmentRepository} and {@link StageRepository}
	 * 
	 * @param equipments must not be {@literal null}
	 * @param stages must not be {@literal null}
	 */
	public EquipmentManagement(EquipmentRepository equipments, StageRepository stages) {
		Assert.notNull(equipments, "EquipmentRepository must not be null!");
		Assert.notNull(stages, "StageRepository must not be null!");
		
		this.equipments = equipments;
		this.stages = stages;
	}
	
	/**
	 * Saves {@link Stage}
	 * 
	 * @param stage must not be {@literal null}.
	 * @return the saved {@link Stage} instance.
	 */
	public Stage saveStage(Stage stage) {
		Assert.notNull(stage, "Stage must not be null!");
		return stages.save(stage);
	}
	
	/**
	 * Returns {@link Equipment} with given id if it exists
	 * 
	 * @param id
	 * @return the {@link Equipment} with the given id or {@literal Optional#empty()} if none found.
	 */
	public Optional<Equipment> findEquipmentById(SalespointIdentifier id) {
		return equipments.findById(id);
	}
	
	/**
	 * Returns {@link Stage} with given id if it exists
	 * 
	 * @param id
	 * @return the {@link Stage} with the given id or {@literal Optional#empty()} if none found.
	 */
	public Optional<Stage> findStageById(SalespointIdentifier id) {
		return stages.findById(id);
	}
		
	/**
	 * Removes {@link Stage} with given id 
	 * 
	 * @param id must not be {@literal null}
	 */
	public void removeStageById(SalespointIdentifier id) {
		Assert.notNull(id, "Id must not be null!");
		stages.deleteById(id);
	}
	
	/**
	 * Returns all {@link Equipment}s currently available in the system
	 *
	 * @return all {@link Equipment} entities
	 */ 
	public Streamable<Equipment> findAllEquipments(){
		return equipments.findAll();
	}
	
}
