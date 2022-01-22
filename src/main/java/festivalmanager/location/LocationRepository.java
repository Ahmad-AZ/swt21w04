package festivalmanager.location;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * A repository interface to manage {@link Location} instances.
 *
 * @author Adrian Scholze
 */
public interface LocationRepository extends CrudRepository<Location, Long> {
	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Integer}
	 * @return all {@link Location}s
	 */
	@Override
	Streamable<Location> findAll();
}
