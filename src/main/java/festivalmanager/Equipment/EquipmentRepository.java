package festivalmanager.Equipment;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface EquipmentRepository extends CrudRepository<Equipment, SalespointIdentifier> {
	@Override
	Streamable<Equipment> findAll();
}
