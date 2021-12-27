package festivalmanager.Equipment;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface StageRepository extends CrudRepository<Stage, SalespointIdentifier> {
	@Override
	Streamable<Stage> findAll();
}