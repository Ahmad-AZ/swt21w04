package festivalmanager.Equipment;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * A repository interface to manage {@link Equipment} instances
 *
 * @author Tuan Giang Trinh
 */
public interface EquipmentRepository extends CrudRepository<Equipment, SalespointIdentifier> {
	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Integer}
	 * @return all equipments
	 */
	@Override
	Streamable<Equipment> findAll();
}
