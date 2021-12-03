package festivalmanager.Equipment;

import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EquipmentManagement {
	
	private final EquipmentRepository equipments;
	
	public EquipmentManagement(EquipmentRepository equipments) {
		this.equipments = equipments;
	}
	
	/**
	 *
	 * @return all equipment entities
	 */ 
	public Streamable<Equipment> findAll(){
		return equipments.findAll();
	}
	
	public Optional<Equipment> findById(Long id) {
		return equipments.findById(id);
	}
}
