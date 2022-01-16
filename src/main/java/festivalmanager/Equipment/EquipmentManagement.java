package festivalmanager.Equipment;

import java.util.Optional;

import festivalmanager.hiring.Artist;
import festivalmanager.hiring.ArtistRepository;
import festivalmanager.hiring.HiringManagement;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	 * @param equipments must not be {@literal null}
	 * @param stages must not be {@literal null}
	 */
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
	public Streamable<Equipment> findAllEquipments(){
		return equipments.findAll();
	}
	
	public Streamable<Stage> findAllStages(){
		return stages.findAll();
	}
}
