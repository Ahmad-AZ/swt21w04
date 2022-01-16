package festivalmanager.Equipment;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * A repository interface to manage {@link Stage} instances
 *
 * @author Adrian Scholze
 */
public interface StageRepository extends CrudRepository<Stage, SalespointIdentifier> {
	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Integer}
	 * @return alle stages
	 */
	@Override
	Streamable<Stage> findAll();
}