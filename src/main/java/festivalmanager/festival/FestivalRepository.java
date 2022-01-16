package festivalmanager.festival;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * A repository interface to manage {@link Festival} instances.
 *
 * @author Adrian Scholze
 */
public interface FestivalRepository extends CrudRepository<Festival, Long> {
	
	@Override
	Streamable<Festival> findAll();
 
}
