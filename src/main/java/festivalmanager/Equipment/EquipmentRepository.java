package festivalmanager.Equipment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
	@Override
	Streamable<Equipment> findAll();

}
