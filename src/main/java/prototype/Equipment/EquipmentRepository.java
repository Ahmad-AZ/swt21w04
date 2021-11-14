package prototype.Equipment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import prototype.location.Location;

public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
	@Override
	Streamable<Equipment> findAll();

}
